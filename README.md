# When an exception occurs in the logs accumulated in Elasticsearch, send a notification to a Slack channel.

This application is designed to facilitate monitoring for the occurrence of exceptions.

## Background
It was created to reduce the inconvenience of having to connect to the server and view the log every time an exception occurs, create an index with kibana, or search with elasticsearch viewer.

> [!IMPORTANT] 
> Currently, the document structure is not easily customizable, but we plan to refactor it in the future so that it can be used with any document through simple configuration.

## Requirements

* This project requires Java 8 or later.
* spring boot version 3.x
* slack-api-client 1.29.2
* elasticsearch-java 7.17.15

## Usage

* You can configure the elasticsearch ip and Slack details in the application.yml file.
  * You cna choose delivery time schedule.
    * You can set in detail which logs to filter. (Currently, the filter conditions are set as default according to the document structure of my project.)
       ```
        monitoring:
          es:
            search:
              index: logs_
              services: "+atest +btest"
              max-count: 30
              during-minutes: 2
              message: "*exception*"
              exclude-messages: "+400"
              view-page-url: http://localhost:9200
        ```
    * For example, the above setting filters only data containing exceptions among messages that include service column values of 'atest' and 'btest' in documents starting with "logs, 
    * but excludes messages containing '400' error. And a maximum of 30. 
    * The setting is “target and search in 2-minute increments.”

  * run application
      * build this project and run EsLogNotifierApplication.class
      * [OR] java -jar es_log_notifier.jar
      * execute api
        ```
          curl -X POST 'http://localhost:8080/eslog/notify'
        ```
  * [OR] you can use spring schedule. you just set the job time.
    ```
       jobs:
         cronSchedule: "0 0/2 * * * 1-5"
    ```

## Output snapshot
* message
![slack_message](https://tnfhrnsss.github.io/docs/sub-projects/img/es_log_notifier_message.png)

* detail log view
![slack_log_detail](https://tnfhrnsss.github.io/docs/sub-projects/img/es_log_detail_view.png)


## Blog reference

For further reference, please consider the following sections:

* [blog](https://tnfhrnsss.github.io/docs/sub-projects/elasticsearch_log_notification/)

