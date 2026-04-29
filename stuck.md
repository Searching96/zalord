1. WebSocket did not initiated:
    - Problem: Spring Boot config yml has 'context-path: /api/v1', which means all API calls to this backend must have a '/api/v1' after the domain name and before the URI, yet frontend set the URL to 'http://domain_name/ws'. Besides, STOMP do no show any error log even though we have manual try catch implemented.
    - Solution: Found out that there is a 'debug' properties in STOMP ws that allows us to print out internal debug logs, so I enabled it and found out the URL is mismatched.
    - Key takeaways: Libraries may provide internal debug loggers for their components.
2. Confused with API URL definition:
    - Problem: Thought it is weird when the 'GetUserChats' endpoint, which is an endpoint belong to the messaging context, has the '/users' prefix.
    - Solution: Found out that it is right since API URL is how external system communicate with our system, not indicate our system's architecture or context. If we name the URL '/chats?userId={userId}', it implies a global search: "Look through every single chat room in the entire database, and filter out the ones where this user is not a member.". But base on the 'resource owner ship model', naming this API URL as '/users/{userId}/chats' should be the best fit since it tells: "Go directly to this specific user's profile, and hand me their personal collection of chats.".
3. Data retrieval phases:
    - Problem: Confused about these components: Result, Response, Request, Retrieved Data (from DB)
    - Solution: 
        1. Result: In UseCase layer, it returns the data that we want to return to the client
        2. Response: Do the record mapping by giving the response entity named key, like {'chatId': uuid} instead of {uuid}. This approach gives better maintainability. Response is usually the same with Result if there is one.
        3. Request: Uses to map HTTP request body, only exists if the request has a request body (not the Path param or Query param).