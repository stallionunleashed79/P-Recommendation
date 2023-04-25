count=1
while true
do
echo "Checking the health endpoint..."
sleep 1
RC=$(curl -sL -w "%{http_code}\\n"  --request GET 'localhost:8081/actuator/health' --header 'Authorization: Bearer' -o /dev/null)
echo Return Code ${RC}
sleep 1
echo

if [ ${RC} = 200 ];
then
echo "Container is healthy..."
sleep 1
echo "Exiting now..."
sleep 2
break

elif [ $(docker inspect -f '{{.State.Running}}' reco) =  "false" ];
then
echo "Reco application container is exited and not running anymore, printing logs and exiting now..."
docker logs reco
sleep 2
exit 1

elif [ ${count} -gt 20 ];
then
echo "Too many attempts, no '200' success code returned, printing logs and exiting now..."
docker logs reco
sleep 2
exit 1
fi
((count++))
done