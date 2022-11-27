JAR_DIR='/home/ubuntu/'$2

echo "> Auto batch deploy starting...."

mkdir $JAR_DIR
cd $JAR_DIR

ORIGIN_JAR_PATH= "${JAR_DIR}/*.jar"
ORIGIN_JAR_NAME=$(basename ${ORIGIN_JAR_PATH})
TARGET_PATH="${JAR_DIR}/batch-application.jar"

echo "> Do deploy :: $1"
echo "  > 배포 JAR: "${ORIGIN_JAR_NAME}

echo "  > sudo ln -s -f ${JAR_DIR}${ORIGIN_JAR_NAME} ${TARGET_PATH}"
sudo ln -s -f ${JAR_DIR}${ORIGIN_JAR_NAME} ${TARGET_PATH}

echo "> Batch deploy Complete ! !"
