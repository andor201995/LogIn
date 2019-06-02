# LogIn
Simple exercise on login use case of application

### EndPoint:

---
#### REQUEST

- POST  /v1/login 
- header: authorization->authKey (Base64 encoding of email and password)
#### RESPONSE

- JWT token in response example
```
{
 "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Inh5ekBtYWlsLmNvbSIsInBhc3N3b3JkIjoiWFlaIiwiaWF0IjoxNTE2MjM5MDIyfQ.afp1lPZAQUCQaoCJ1VsawPmjuNPoijhW4SDMleOFWHE"
}
```

---

#### REQUEST

- GET  /v1/user 
- header: auth_token->token (JWT response from login)

#### RESPONSE
```
{
 "name": "andor"
}
```

---

Reference:
 - [MockingREST API](https://www.mockable.io)
 - [JWT.io](https://jwt.io/)
