## gift certificate project for Clevertec
### Technologies:
- #### stack:
<pre>
    implementation 'org.mapstruct:mapstruct:1.5.3.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.3.Final'
    runtimeOnly 'org.postgresql:postgresql'

    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.liquibase:liquibase-core'
</pre>
- #### test:
<pre>
    testImplementation "org.springframework.boot:spring-boot-starter-webflux"
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation "org.testcontainers:postgresql:1.17.6"
    testImplementation "org.testcontainers:junit-jupiter:1.17.6"
</pre>>
### Profiles:
  - Default
    - src/main/resources/application.yml 
  - Production
    - -Dspring.profiles.active=prod
    - src/main/resources/application-prod.yml
  - Development
    - -Dspring.profiles.active=dev
    - src/main/resources/application-dev.yml

### Configurations
- spring:
  - datasource:
    - url: - connection url
    - username: - connection username
    - password: - connection password

## Endpoints:
  - ### Tags:
    - #### GET byId:
      - url: http://localhost:8080/api/v1/tags/{{TAG_ID}}
        - response:
          - status: 200
          - body:
            <pre>
              {
                "id": 1,
                "name": "#1"
              }
            </pre>
    - #### GET all:
      - url: http://localhost:8080/api/v1/tags
        - response:
          - status: 200
          - body:
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
    - #### GET popular:
      - url: http://localhost:8080/api/v1/tags/popular
        - response:
          - status: 200
          - body:
            <pre>
              {
                "id": 3,
                "name": "#3"
              }
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
    - #### PATCH:
      - url: http://localhost:8080/api/v1/certificates/{{ID}}
        - request body:
          <pre>
            {
              "field":"duration",
              "value":"11"
            }
          </pre>
        - response status: 201
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
  - ### Orders:
    - #### GET findAllOrdersByUserId:
      - url: http://localhost:8080/api/v1/orders/{{user_id}}
        - params:
          - page - number if page
          - size - size of page
        - response:
          - status: 200
          <pre>
            {
              "content": [
                {
                  "id": 1,
                  "giftCertificateId": 1,
                  "price": 1,
                  "purchase": "2023-05-10T00:31:23.12256"
                },
                {
                  "id": 2,
                  "giftCertificateId": 1,
                  "price": 1,
                  "purchase": "2023-05-10T00:31:23.12256"
                },
                {
                  "id": 12,
                  "giftCertificateId": 4,
                  "price": 4,
                  "purchase": "2023-05-10T00:31:23.12256"
                },
              ],
              "size": 20,
              "number": 0,
              "first": true,
              "last": true,
              "totalPages": 1,
              "totalElements": 6,
              "numberOfElements": 6,
              "empty": false
            }
          </pre>
  - ### Orders:
    - #### POST createNewOrder:
      - url: http://localhost:8080/api/v1/orders
        - request:
          - body: 
            <pre>
              {
                "userId":2,
                "certificateId":16
              }
            </pre>
        - response:
          - status: 201
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
