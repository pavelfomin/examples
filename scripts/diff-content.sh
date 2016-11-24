##
# Returns a diff status of the comparison between the current content of the http response and a previously saved one. 
##

#set -x #echo commands
set -e #exit on error

if [ -z "$1" ]
then
	echo "Usage: $0 <url> [<sed expression>]"
	exit 1
fi

url=$1
sedExpression=$2

contentBaseDir=${TMP}
contentFileName=content.html

dateTime=$(date +%Y-%m-%d.%H.%M.%S)
contentDir=${contentBaseDir}/${url##*://}
historyDir=${contentDir}/history
previousContent=${contentDir}/${contentFileName}
currentContent=${contentDir}/${contentFileName}.${dateTime}
logFile=${historyDir}/${contentFileName}.${dateTime}.log
diffFile=${historyDir}/${contentFileName}.${dateTime}.diff

mkdir -p ${historyDir}

curl -v --fail "${url}" 2>${logFile} 1>${currentContent} 

if [ ${sedExpression} ]
then
	mv ${currentContent} ${currentContent}.raw
	sed -e "${sedExpression}" ${currentContent}.raw > ${currentContent}
	rm  ${currentContent}.raw
fi

if [ -e "${previousContent}" ]
then
	set +e
	diff ${currentContent} ${previousContent} > ${diffFile}
	diffStatus=$?
	set -e
	
	if (( ${diffStatus} == 0 )) 
	then
		echo "no differences found between ${currentContent} and ${previousContent}"
		rm ${currentContent}
	else
		echo "content ${currentContent} and ${previousContent} are different"
		mv ${previousContent} ${previousContent}.${dateTime}.orig
		mv ${currentContent} ${previousContent}
	fi
	exit ${diffStatus}
else
	echo "${previousContent} does not exist, initialize it with ${currentContent}"
	mv ${currentContent} ${previousContent}
fi
