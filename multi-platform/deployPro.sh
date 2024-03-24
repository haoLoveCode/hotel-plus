#deployPro.sh
if [ -z $1 ]; then
  echo "请指明需要部署的项目"
  exit
fi

PROJRCT_NAME=hotel-plus

#项目打包的位置
BUILD_PATH=/opt/$PROJRCT_NAME/multi-platform/
#项目构建的位置
DEPLOY_PATH=/opt/$PROJRCT_NAME/codeDeploy/
#项目代码的位置
CODE_PATH=/opt/$PROJRCT_NAME/

cd $CODE_PATH
git pull origin dev

echo "编译路径：$BUILD_PATH"
cd $BUILD_PATH
mvn clean package

if [ -d $DEPLOY_PATH$1 ]; then
  cd $DEPLOY_PATH$1
  chmod +x sbin/*.sh & sh sbin/shutdown.sh
  #此处直接删除部署目录
  rm -rf $DEPLOY_PATH$1
fi

echo "项目打包目录:"$BUILD_PATH$1
echo "项目运行目录:"$DEPLOY_PATH
echo "项目运行具体目录:"$DEPLOY_PATH$1

#如果部署目录不存在则创建一个
if [ ! -d $DEPLOY_PATH ]; then
  mkdir $DEPLOY_PATH
fi

tar -zxvf $BUILD_PATH$1.tar.gz -C $DEPLOY_PATH
cd $DEPLOY_PATH$1
chmod +x sbin/*.sh & sh sbin/startup.sh dev

#当前线程睡眠3秒
sleep 3s
#查看日志
tail -100f $DEPLOY_PATH$1/logs/logs.log
