JAR_DIR='/home/ubuntu/'$2

cp keewe-batch/build/libs/keewe-batch-0.0.1-SNAPSHOT.jar ${JAR_DIR}

echo "> Auto batch deploy starting...."

mkdir -p $JAR_DIR
cd $JAR_DIR

chmod 770 ${JAR_DIR}/$1-0.0.1-SNAPSHOT.jar
ORIGIN_JAR_PATH="${JAR_DIR}/$1-0.0.1-SNAPSHOT.jar"
ORIGIN_JAR_NAME=$(basename ${ORIGIN_JAR_PATH})
TARGET_PATH="${JAR_DIR}/batch-application.jar"

echo "> Do deploy :: $1"
echo "  > 배포 JAR: "${ORIGIN_JAR_NAME}

echo "  > sudo ln -s -f ${JAR_DIR}/${ORIGIN_JAR_NAME} ${TARGET_PATH}"
ln -s -f "${JAR_DIR}/${ORIGIN_JAR_NAME} ${TARGET_PATH}"

echo "> Batch deploy Complete ! !"