import time

import requests
import pprint

host = 'http://localhost:1080'

# MOCKSERVER ENDPOINTS
get_all_registrations_endpoint = "/mockserver/retrieve?type=ACTIVE_EXPECTATIONS"
get_logs_endpoint = "/mockserver/retrieve?type=LOGS"
clear_expectation = "/mockserver/clear?type=EXPECTATIONS"
reset_endpoint = "/mockserver/reset"

# Dealer Details ENDPOINTS
get_endpoint = '/plm/v1/metadata/artifacts'

pp = pprint.PrettyPrinter(width=180)


def hit_api(mockserver_request_method, mockserver_path, target_request_method, target_path, response_code, response_body):
  body = generate_register_body(target_request_method, target_path, response_code, response_body)
  return requests.request(method=mockserver_request_method, url=host + mockserver_path, json=body)


def generate_register_body(method, path, response_code, response_body):
  return {"httpRequest": {
    "method": method,
    "path": path,
    "queryStringParameters": {
      "artifactName": ["grief-test-external-bucket/file.json"]
    }
  },
    "httpResponse": {
      "statusCode": response_code,
      "body": response_body
    }}


get_body = {
  "parentArtifactName": [
    {
      "artifactName": "biconnector/prod/M450/Equipment/Equipment/input/2019/06/01",
      "artifactLocation": "s3://biconnector/prod/M450/Equipment/Equipment/input/2019/06/01",
    }
  ],
  "artifactDetails":
    {
      "artifactName": "grief-test-external-bucket/file.json",
      "artifactLocation": "s3://grief-test-external-bucket/file.json",
    }
  ,
  "childDetails": [
    {
      "artifactName": "biconnector/prod/M450/Equipment/Equipment/input/2019/06/01",
      "artifactLocation": "s3://biconnector/prod/M450/Equipment/Equipment/input/2019/06/01",
    }
  ],
  "subArtifacts": [
    {
      "artifactName": "biconnector/prod/M450/Equipment/Equipment/input/2019/06/01/eq_123.parquet",
      "artifactLocation": "s3://biconnector/prod/M450/Equipment/Equipment/input/2019/06/01/eq_123.parquet",
    },
    {
      "artifactName": "biconnector/prod/M450/Equipment/Equipment/input/2019/06/01/eq_234.parquet",
      "artifactLocation": "s3://biconnector/prod/M450/Equipment/Equipment/input/2019/06/01/eq_234.parquet",
    }
  ]
}



# # Resets the mockserver
# print("\n>>>Call to reset the mockserver.")
# reset_response = requests.request(method="PUT", url=host+reset_endpoint)
# print("Response: " + str(reset_response.status_code))
# time.sleep(1)
#
# # Creates an expectation
# print("\n>>>Call to create an expectation.")
# register_response = hit_api("PUT", "/expectation", "GET", get_endpoint + '/relationships/details', 200, get_body)
# print("Response: " + str(register_response.status_code))
# time.sleep(1)

# print("\n>>>Call to create an expectation.")
# register_response = hit_api("PUT", "/expectation", "GET", get_endpoint + '/grief-test-external-bucket%23file1.json' + '/details', 200, get_body)
# print("Response: " + str(register_response.status_code))
# time.sleep(1)

# Returns registered expectations
print("\n>>>Call to get all registered expectations.")
get_registered_responses = requests.request(method="PUT", url=host+get_all_registrations_endpoint)
pprint.pprint(get_registered_responses.json())
time.sleep(1)

# Returns logs
print("\n>>>Call to get logs.")
get_registered_logs = requests.request(method="PUT", url=host+get_logs_endpoint)
pp.pprint(get_registered_logs.content)
time.sleep(1)



