# BB1 Backend Home assignment

## Task
Implement an API for a SaaS messaging application.

## API

### User creation
The API endpoint is designed to allow users to create an account.

Endpoint: `/api/users`

This endpoint accepts HTTP POST requests with the following parameters in the request body:

| Parameter | Type   | Description                                    |
|-----------|--------|------------------------------------------------|
| userName  | string | The desired username for the new user account. |
| password  | string | The desired password for the new user account. |

The API should validate that all parameters are present and have the correct data type. If any parameter is missing or has an invalid data type, the API should return an error response with a suitable HTTP status code (e.g. 400 Bad Request).

If all parameters are present and valid, the API should create a new user account and return a success response with a suitable HTTP status code (e.g. 201 Created).

### Retrieving user data

Endpoint: `/api/users/{userId}`

This endpoint accepts HTTP GET requests with the following query parameters:

| Parameter | Type   | Description                                        |
|-----------|--------|----------------------------------------------------|
| userId    | string | The unique identifier of the user to be retrieved. |

The API should validate that the access token is present and valid. If the access token is missing or invalid, the API should return an error response with a suitable HTTP status code (e.g. 401 Unauthorized).

If the authenticated user has permission, the API should retrieve the user details and return them in the response body:

| Parameter           | Type   | Description                                                                         |
|---------------------|--------|-------------------------------------------------------------------------------------|
| userId              | string | The unique identifier of the user to be retrieved.                                  |
| userName            | string | The desired username for the new user account.                                      |
| averageResponseTime | float  | The average response time in seconds for the user based on their messaging history. |

The averageResponse parameter should represent the average response time in seconds for the user based on their messaging history. This would involve calculating the time between when a user receives a message and when they send a reply and calculating the average response time based on the user's messaging history.


### Message send

The API endpoint is designed to allow authenticated users to send messages.

Endpoint: `/api/messages`

This endpoint accepts HTTP POST requests with the following parameters in the request body:

| Parameter   | Type   | Description                                                 |
|-------------|--------|-------------------------------------------------------------|
| message     | string | The text of the message.                                    |
| senderId    | string | The unique identifier of the user who sends the message.    |
| recipientId | string | The unique identifier of the user who receives the message. |

### Message retrieval

The API endpoint is designed to allow authenticated users to retrieve their messages.

Endpoint: `/api/users/{userId}/messages`

This endpoint accepts HTTP GET requests with an access token in the request headers with the following query parameters:

| Parameter | Type   | Description                                 |
|-----------|--------|---------------------------------------------|
| dateFrom  | string | The minimum date of the messages to return. |
| dateTo    | string | The maximum date of the messages to return. |

The dateFrom and dateTo parameters should be provided in ISO 8601 format. For example: `2023-02-01T12:00:00Z`

The API should validate that the dateFrom and dateTo parameters have the correct data type. If any parameter has an invalid data type, the API should return an error response with a suitable HTTP status code (e.g. 400 Bad Request).

If both dateFrom and dateTo parameters are present, the API should retrieve the messages from the database that fall within the specified date range. If only one of the parameters is present, the API should retrieve the messages that match that parameter. If neither parameter is present, the API should retrieve all messages.

The API should validate that the access token is present and valid. If the access token is missing or invalid, the API should return an error response with a suitable HTTP status code (e.g. 401 Unauthorized).

The API should return a success response with a suitable HTTP status code (e.g. 200 OK).

Model of the message:

| Field       | Type   | Description                                          |
|-------------|--------|------------------------------------------------------|
| message     | string | The text of the message.                             |
| senderId    | string | The identifier of the user who sends the message.    |
| recipientId | string | The identifier of the user who receives the message. |
| createdAt   | string | The date and time the message was sent.              |

### Login

The API endpoint is designed to allow users to log in to their account using a username and password.

Endpoint: `/api/login`

This endpoint accepts HTTP POST requests with the following parameters in the request body:

| Parameter | Type   | Description               |
|-----------|--------|---------------------------|
| userName  | string | The username of the user. |
| password  | string | The password of the user. |

The API should validate that both the username and password parameters are present and have the correct data type. If any parameter is missing or has an invalid data type, the API should return an error response with a suitable HTTP status code (e.g. 400 Bad Request).

If both parameters are present and valid, the API should verify the username and password combination with the database. If the combination is correct, the API should generate and return an access token for the user in the response body.

The API should return the following response body:

| Parameter   | Type   | Description                            |
|-------------|--------|----------------------------------------|
| accessToken | string | The access token generated by the API. |

### Logout

The API endpoint is designed to allow authenticated users to log out of their account.

Endpoint: `/api/logout`

This endpoint accepts HTTP POST requests with an access token in the request headers.

The API should validate that the access token is present and valid. If the access token is missing or invalid, the API should return an error response with a suitable HTTP status code (e.g. 401 Unauthorized).

If the access token is present and valid, the API should invalidate the token and return a success response with a suitable HTTP status code (e.g. 200 OK).


## Pushing it further (aka optional requirements)
1. The application should be designed to support multiple customers or organizations, with each customer having their own isolated data. To achieve this, you need to include a unique identifier for the customer (e.g. customerId) in each API call and ensure that the data is properly isolated and secured for that customer.
2. Implement a real-time messaging feature using WebSockets for instant messaging. You can use a library like Socket.IO to implement this.

## Hints
* Feel free to use any libraries