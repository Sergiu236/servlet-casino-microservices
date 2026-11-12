# 01 - Servlet Casino: Specs

A small casino built from three servlet services, each deployed in a different application server and communicating over HTTP.

## Services

| Service | Server | Role |
|---|---|---|
| `ui-service` | Tomcat | The only service the browser talks to. Servlets + JSP render the pages, keep the user in the `HttpSession`, and call the other two services. |
| `wallet-service` | Jetty | Holds user balances (in memory). Creates accounts, debits and credits chips. |
| `games-service` | WildFly | Runs the game logic (e.g. slots) and returns the result and payout. |

```
Browser --> ui-service (Tomcat) --> wallet-service (Jetty)
                    |
                    +-------------> games-service (WildFly)
```

## End-to-end workflow

1. **Login:** the user enters a username on the login page. `ui-service` stores it in the session and asks `wallet-service` to create the account (or return the existing one). New accounts start with a fixed balance.
2. **Play:** the user places a bet.
   1. `ui-service` asks `wallet-service` to debit the bet. If the balance is too low, it stops and shows an error.
   2. `ui-service` asks `games-service` to play the game and gets the result.
   3. If the user won, `ui-service` asks `wallet-service` to credit the payout.
3. **Result:** `ui-service` renders a JSP with the game result and the new balance.

## Notes

- Services communicate with plain HTTP requests (JSON responses); no token, since only `ui-service` is exposed to the browser.
- State is kept in memory, so balances reset on restart (a database comes in homework 02).
- All three servers run at the same time and the WARs are deployed manually, not from the IDE (see `../deploy/`).
