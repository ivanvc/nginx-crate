(ns pallet.crate.nginx-test
  (:use pallet.crate.nginx)
  (:require
   [pallet.build-actions :as build-actions]
   [pallet.action.directory :as directory]
   [pallet.action.remote-file :as remote-file]
   [pallet.action.file :as file])
  (:use clojure.test
        pallet.test-utils))

(deftest site-test
  []
  (is (= (first
          (build-actions/build-actions
           {}
           (directory/directory "/etc/nginx/sites-available")
           (directory/directory "/etc/nginx/sites-enabled")
           (remote-file/remote-file
            "/etc/nginx/sites-enabled/mysite"
            :content "\n\nserver {\n  listen       80;\n  server_name  localhost;\n\n  access_log  /var/log/nginx/access.log;\n  \n  \n\nlocation / {\n  root /some/path;\n  index index.html index.htm;\n  \n  \n  \n  \n  \n}\n\nlocation /a {\n  \n  \n  proxy_pass localhost:8080;\n  \n  proxy_set_header a;\nproxy_set_header b;\n\n  \n  \n}\n\n\n  \n}\n")
           (file/file
            "/etc/nginx/sites-available/mysite" :action :delete :force true)))
         (first
          (build-actions/build-actions
           {:server {:group-name :n :image {:os-family :ubuntu}}}
           (site "mysite"
                 :locations [{:location "/" :root "/some/path"
                              :index ["index.html" "index.htm"]}
                             {:location "/a"
                              :proxy_pass "localhost:8080"
                              :proxy_set_header ["a" "b"]}]))))))
