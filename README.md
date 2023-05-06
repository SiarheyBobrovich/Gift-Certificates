## gift certificate project for Clevertec
### Technologies:
- #### stack:
<pre>
      implementation 'org.mapstruct:mapstruct:1.5.3.Final'
      annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.3.Final'
      runtimeOnly 'org.postgresql:postgresql:42.5.1'
      compileOnly 'jakarta.servlet:jakarta.servlet-api:6.0.0'
    
      implementation 'org.hibernate.orm:hibernate-hikaricp:6.2.1.Final'
      implementation 'org.springframework:spring-webmvc:6.0.8'
      implementation 'org.hibernate:hibernate-core:5.6.1.Final'
      implementation 'org.yaml:snakeyaml:2.0'
      implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.0-rc3'
      implementation 'org.slf4j:slf4j-api:2.0.7'
      implementation 'org.slf4j:slf4j-simple:2.0.7'
      implementation 'org.hibernate.validator:hibernate-validator:6.0.0.Final'
      implementation 'jakarta.validation:jakarta.validation-api:3.0.2'
      implementation 'org.glassfish:javax.el:3.0.0'
</pre>
- #### test:
<pre>
      testImplementation 'org.junit.jupiter:junit-jupiter:5.9.1'
      testImplementation "org.testcontainers:postgresql:1.17.6"
      testImplementation "org.testcontainers:junit-jupiter:1.17.6"
      testImplementation 'org.assertj:assertj-core:3.24.2'
      testImplementation 'org.mockito:mockito-junit-jupiter:5.3.0'
</pre>>
### Profiles:
  - Default
    - src/main/resources/application.yml 
  - Production
    - -Dspring.profiles.active=prod
    - src/main/resources/application.yml
  - Development
    - -Dspring.profiles.active=dev
    - src/main/resources/application.yml

### Configurations
- spring:
  - datasource:
    - url: - connection url
    - username: - connection username
    - password: - connection password
  - hibernate:
    - show_sql: - print sql query to console
    - format_sql: - format printed sql query

## Endpoints:
  - ### Tags:
    - #### GET byId:
      - url: http://localhost:8080/api/v1/tags/{{TAG_ID}}
        - response:
          <pre>
          {
            "id": 1,
            "name": "#1"
          }
          </pre>
    - #### GET all:
      - url: http://localhost:8080/api/v1/tags
        - response:
          <pre>
            [
              {
                "id": 1,
                "name": "#1"
              },
              {
                "id": 2,
                "name": "#2"
              }
            ]
          </pre>>
    - #### POST:
      - url: http://localhost:8080/api/v1/tags
        - request body:
          {
            "name": "#dada"
          }
        - response status: 201
    - #### PUT:
      - url: http://localhost:8080/api/v1/tags/{{TAG_ID}}
        - request body:
          {
            "name": "#dada"
          }
        - response status: 201
    - #### DELETE:
      - url: http://localhost:8080/api/v1/tags/{{TAG_ID}}
        - response status: 204
  - ### Certificates:
    - #### GET byId:
      - url: http://localhost:8080/api/v1/certificates/{{ID}}
        - response:
          - status: 200
          <pre>
            {
              "id": 2,
              "name": "second",
              "price": 2.22,
              "description": "second certificate",
              "duration": 22,
              "createDate": "2023-04-02T00:00",
              "lastUpdateDate": "2023-04-25T02:05:08.926289",
              "tags": [
                {
                  "id": 2,
                  "name": "#2"
                },
                {
                  "id": 3,
                  "name": "#3"
                }
              ]
            }
          </pre>
    - #### GET all:
      - url: http://localhost:8080/api/v1/certificates/{{ID}}
        - response:
          - status: 200
          <pre>
            [
              {
                "id": 2,
                "name": "second",
                "price": 2.22,
                "description": "second certificate",
                "duration": 22,
                "createDate": "2023-04-02T00:00",
                "lastUpdateDate": "2023-04-25T02:05:08.926289",
                "tags": [
                  {
                    "id": 2,
                    "name": "#2"
                  },
                  {
                    "id": 3,
                    "name": "#3"
                  }
                ]
              }
            ], ...
          </pre>
    - #### GET findBy:
      - url: http://localhost:8080/api/v1/certificates/findBy?part=th&sort=name&tag=#3
        - params: optional
          - part: part of name/description
          - sort: name/createDate [asc/desc]
            - format: {"name", "name,desc"}
              - "name" == "name,asc"
          - tag: tag name
        - response:
          - status: 200
            <pre>
              [
                {
                  "id": 3,
                  "name": "third",
                  "price": 3.33,
                  "description": "third certificate",
                  "duration": 33,
                  "createDate": "2023-04-03T00:00:00",
                  "lastUpdateDate": "2023-04-25T02:05:08.926289",
                  "tags": [
                    {
                      "id": 3,
                      "name": "#3"
                    }
                  ]
                }
              ]
            </pre>
     - #### POST:
      - url: http://localhost:8080/api/v1/certificates
        - request body:
          <pre>
            {
              "name": "new Cert",
              "price": 0.00,
              "description": "New certificate",
              "duration": 11,
              "tags": [
                {
                  "name": "#2"
                },
                {
                  "name": "#6"
                }
              ]
            }
          </pre>
        - response:
          - status: 201
     - #### PUT:
      - url: http://localhost:8080/api/v1/certificates
        - request body:
          <pre>
            {
              "name": "Updated",
              "price": 99,
              "description": "Updated certificate",
              "duration": 99,
              "tags": [
                {
                  "name": "#11"
                },
                {
                  "name": "#1"
                }
              ]
            }
          </pre>
        - response:
          - status: 201
     - #### DELETE:
      - url: http://localhost:8080/api/v1/certificates/{{ID}}
        - response:
          - status: 204
  - ### Users:
    - #### GET byId:
      - url: http://localhost:8080/api/v1/users/{{ID}}
        - response:
          - status: 200
          <pre>
            {
                "id": 1,
                "firstName": "Siarhey",
                "lastName": "Bobrovich",
                "orders": []
            }
          </pre>
    - #### GET all:
      - url: http://localhost:8080/api/v1/users?page=1&size=1
        - params: 
          - page - number if page
          - size - size of page
        - response:
          - status: 200
          <pre>
            {
                "content": [
                    {
                    "id": 2,
                    "firstName": "Egor",
                    "lastName": "Efimov",
                    "orders": []
                    }
                ],
                "size": 1,
                "number": 1,
                "first": false,
                "last": true,
                "totalPages": 2,
                "totalElements": 2,
                "numberOfElements": 1,
                "empty": false
            }
          </pre>
### Errors:
  - code:
    - tags: ***01
    - certificates: ***02
      <pre>
        {
          "errorMessage": "Requested resource not found (id = 2)",
          "errorCode": 404002
        }
      </pre>
