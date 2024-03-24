#!/bin/bash

npm run build

tar -zcvf dist.tar.gz dist


des_pass=95sM*FrRq8ZX8pCW
expect -c "
spawn  scp  dist.tar.gz root@47.92.207.93:/usr/share/nginx/html/
 
expect \"password:\"
send \"${des_pass}\r\"
expect eof
"

ip=47.92.207.93
user=root
password=95sM*FrRq8ZX8pCW

expect <<EOF
    set timeout 30
    spawn ssh $user@$ip
    expect {
        "yes/no" { send "yes\n";exp_continue }
        "password" { send "$password\n" }
    }

    expect "]#" { send "/root/devvue.sh\r" }
    expect "]#" { send "exit\n" }
    expect eof
EOF