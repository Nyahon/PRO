#!/bin/bash

source ../config/config.sh

serverName="daryllserver"
imageTar="dockerDaryllServer.tar"
imagePort=2613

authors() { echo "=====DARYLL=======";
            echo "Dejvid    Muaremi";
            echo "Aurélien  Siu";
            echo "Romain    Gallay";
            echo "Yohann    Meyer";
            echo "Loïc      Frueh";
            echo "Labinot   Rashiti"; 
            }

usage() { echo "Usage: $0 [-b] [-s ] [-r <port>] [-d <port>][-u] [-h]" 1>&2; exit 1; }

build() { docker build --no-cache-t $serverName . ; }

save()  {   echo "working..."; 
            docker save -o $imageTar $serverName ;
            echo "done."; }

run()   { docker run -d -p $1:$DEFAULT_PORT $serverName; }

runServer()   { ssh dev@daryll.lan.iict.ch docker run -d -p $1:$DEFAULT_PORT $serverName; }

upload() { scp  ./$imageTar dev@daryll.lan.iict.ch:$imageTar ;
           ssh dev@daryll.lan.iict.ch docker load -i $imageTar;               
       }

helpf()  { echo "Welcome in the daryll docker construction helper : ";
           echo "     -a : authors of Daryll";
           echo "     -b : build docker image ";
           echo "     -s : save a tar of docker image ";
           echo "     -u : upload the docker image to our server";
           echo "     -r <port> : run locally, with <port> the port exposed to the world. Usually 2613";
           echo "     -d <port> : same than -r, but on server" ;
       }

while getopts ":absur:d:h" o; do
    echo ${o};
    case "${o}" in
        a)
            authors
            ;;
        b)
            b=${OPTARG}
            build
            ;;
        s)
            s=${OPTARG}
            save $s 
            ;;
        r)
            r=${OPTARG}
            run $r
            ;;
        d)
            d=${OPTARG}
            runServer
            ;;
        u)
            upload
            ;;
        h)
            helpf
            ;;
        *)
            usage
            ;;
    esac
done

