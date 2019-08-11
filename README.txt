INSTRUCTIONS
============

1. Assumptions
--------------

It was assumed that:
- Orders can be created anytime with any starting date.
- Orders created with both starting date and (calculated) expiration date in the past are created as EXPIRED.
- Orders created with both starting date and (calculated) expiration date in the past are created as NOT_STARED.
- Orders created with both starting date in the past or present (today) and (calculated) expiration date in the
  future or present (today) are created as NOT_STARTED.
- NOT_STARTED orders become VALID (activation) when the starting date is reached.
- VALID orders become EXPIRED (expiration) when the (calculated) expiration date is reached.
- When requesting an order from the database and the order does not exists or is not VALID, then and empty response is retrieved.


1. Development
--------------

- The project was developed using TDD as development methodology.
- First two commits in Github is the basic project structure. A maven project with the main POJOs defined.
  The project uses a H2 In memory database in order to save/retrieved the orders.
- Third commit describes the Test classes for the Service, Controller and the scheduled processes. These classes are fully developed, 
  compile OK but fail to execute as the business logic has not been implemented yet. The same H2 database but with a different configuration
  was used to create the integration tests that will test the SQLs that will implement processes to activate or expire orders in the database
- Fourth commit. Business logic was then implemented. Test classes run now ok.
  The activation and expiration of orders was made using two Spring scheduling process located into the OrderService. These processes are configured
  to run every day at mid night. The SQL sentences that activate or expire 


2. Execution
------------
- The main class in the project is called "Orders" and reside in the package com.worldpay.screeningtest.
- The Orders micro-service runs in the default Tomcat port 8287.
- If running in the local machine use the following endpoints:
- Test Creation. Issue a curl command as follows:
  
  curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"description" : "Description of goods or services in the order", "price" : "398.41", "validFrom" : "2018-12-19", "validForYears" : 1, "validForMonths" : 3, "validForDays" : 25}' \
  http://localhost:8287/orders/add

  The micro-service with respond with the data of the added order or with the an error code.
  
- Test retrieval. Issue a curl command as follows

  curl --request GET http://localhost:8287/orders/get?order-id=1
  
  The micro-service with respond with the data requested or with null if the requested order does not exist or has status differen to VALID.

- Test Change of status:

  Create two Orders, one VALID that should expire today or in the past and one NON_STARTED that today or in the past:
  
  curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"description" : "Order to be expired", "price" : "398.41", "validFrom" : "2019-08-01", "validForYears" : 0, "validForMonths" : 0, "validForDays" : 11}' \
  http://localhost:8287/orders/add

  
  curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"description" : "Order to be activated", "price" : "398.41", "validFrom" : "2019-08-11", "validForYears" : 0, "validForMonths" : 6, "validForDays" : 0}' \
  http://localhost:8287/orders/add

  - Take the order ID of each of the created orders. and verify that the status of first one is VALID and NON_STARTED for the second one
  - Wait a day (go home, go dancing, have a drink, etc) for the scheduler to run. It runs at midnight. 
  - Next day the status of the first order must have changed to EXPIRED and the status of the second order to VALID.