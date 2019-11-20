#!/bin/sh
#当前目录
CURR_DIR=`pwd`

#项目根目录
cd `dirname "$0"`/..
APP_HOME=`pwd`
cd $CURR_DIR

#main方法类
MAIN_CLASS=com.cn.summer.api.SummerApiApplication

#==============================================================================
#set JAVA_OPTS
#-Xms:初始堆大小
#-Xmx:最大堆大小,和-Xms设置成一样(一般是内存的一半,不超过32G),避免每次垃圾回收完成后JVM重新分配内存
#-Xss:设置每个线程的堆栈大小
#-Xmn64m 新生代不需要设置,设置了新生代大小相当于放弃了G1为我们做的自动调优
JAVA_OPTS="-server -Xms4096m -Xmx4096m -Xss256k"
#performance Options
#每当JDK版本升级时，你的JVM都会使用最新加入的优化技术
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
#启用一个优化了的线程锁 ,对线程处理自动进行最优调配
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
#使用G1 垃圾收集器
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"
#设置期望达到的最大GC停顿时间指标(100~200)
JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=100"
#JDK8 Metaspace的存储空间取代了永久代(PermGen),Metaspace使用的是本地内存，而不是堆内存
JAVA_OPTS="$JAVA_OPTS -XX:MetaspaceSize=256m"
#限制Metaspace增长的上限,防止因为某些情况导致Metaspace无限的使用本地内存
JAVA_OPTS="$JAVA_OPTS -XX:MaxMetaspaceSize=512m"
#打印命令行参数值
JAVA_OPTS="$JAVA_OPTS -XX:+PrintCommandLineFlags"
#禁止代码中显示调用GC
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
#关闭 省略异常栈信息从而快速抛出
JAVA_OPTS="$JAVA_OPTS -XX:-OmitStackTraceInFastThrow"

#当堆内存空间溢出时输出堆的内存快照。触发条件: java.lang.OutOfMemo-ryError: Java heap space
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
#配合HeapDumpOnOutOfMemoryError参数,输出文件
JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=/opt/data/logs/java.hprof"

#输出GC的详细日志
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#输出GC的时间戳(如 2013-05-04T21:53:59.234+0800)
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDateStamps"
JAVA_OPTS="$JAVA_OPTS -Xloggc:/opt/data/logs/gc.log"

#==============================================================================
#set CLASSPATH
#APP_CLASSPATH="$APP_HOME/app:$APP_HOME/app/lib"

for i in "$APP_HOME"/lib/*.jar
do
    APP_CLASSPATH="$APP_CLASSPATH:$i"
done

APP_CLASSPATH="$APP_CLASSPATH:$APP_HOME/conf"

LOGS_HOME="$APP_HOME/bin/logs"
if [ ! -d "$LOGS_HOME" ]; then
    mkdir "$LOGS_HOME"
fi

#==============================================================================
#psid: 全局变量,进程ID
psid=0
initPsid(){
    psid=`ps -ef | grep $APP_HOME | grep -v "grep" |awk '{ print $2}'`
}

#启动程序
start(){
    initPsid
    if [ -n "$psid" ]; then
        echo "=================================================="
        echo "|         server has already started          |"
        echo "=================================================="
    else
        echo "Starting $MAIN_CLASS ..."
        startup
        initPsid
        if [ -n "$psid" ]; then
            echo "Start [OK] pid=$psid"
        else
            echo "Start [FAILED], $?"
        fi
    fi
}
#优雅停止程序
stop(){
    initPsid
    if [ -n "$psid" ]; then
        echo "Stopping $MAIN_CLASS pid=$psid ..."
        kill -15 $psid
        if [ $? -eq 0 ]; then
            echo "Stop [OK]"
        else
            echo "Stop [FAILED]"
        fi
    else
        echo "=================================================="
        echo "|      WARN: $MAIN_CLASS is not running!      |"
        echo "=================================================="
    fi
}

#组装启动参数
startup(){
    RUN_CMD="\"$JAVA_HOME/bin/java\""
    RUN_CMD="$RUN_CMD -Dlogic.home=\"$APP_HOME\""
    RUN_CMD="$RUN_CMD -classpath \"$APP_CLASSPATH\""
    RUN_CMD="$RUN_CMD $JAVA_OPTS"
    RUN_CMD="$RUN_CMD $MAIN_CLASS $@"
    RUN_CMD="$RUN_CMD > /dev/null 2>&1 &"
    #打印控制
    #RUN_CMD="$RUN_CMD > \"$APP_HOME/bin/logs/console.log\" 2>&1 &"
    #echo $RUN_CMD
    eval $RUN_CMD
}


#================================================================================
#根据参数调用方法
case "$1" in
    'start')
        start
        ;;
    'stop')
        stop
        ;;
    'restart')
        stop
	    sleep 1
        start
        ;;
    *)

    echo "Usage $0 { start | stop | restart }"
    exit
esac
exit 0

#==============================================================================