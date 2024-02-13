# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

UML Sequence Diagram of server design:
[link](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv3eQvqc7ta7dUzyJ9vlyrjz5zU6jAAGJWeIAWWNB7uR-dY-hUS3AalMDXB55XxXxlVVWdNTfbE3WeE82Q5C9SVNTNwz9SoRTFFCoJ1T9enYKIUN9SNl1UXCRwIi0iP9EcY38eMk1TdNCOzXM0HzbRdAMYwdBQO1Ky0fRmFrbxfEwOim16Vs+DPJI3jSdIuwkHs8mYv0o1HPCJyrQTRlU9Al09UjpRdIC1BQBAThQKjdMo7MtXfaEej1aSvlk405zDKj70fF8sMA+kyMMxFkVREiY3ImAkRRdTaMbBMYBTZNMDzAtOOLQYZArYYYAAcR5R5hPrMS4oZCLohyt4O3SLQeRU2y1Joj1xwRGBkAcPKygkGysz9AzmtXEycWOMBrL0tB7Og49XjPH5POzbznzDbCPya78YDA1DiMMgbD1M4aOskUYJpw2Dpq+WaACoFpfGqymWxzVvwmBbpQD5hj-FdAq6I4eTehwYtw8T4sS9MXr+sBWPYwsuKMbA9CgbALPgEDVAOjwRIbAJmFW1sEmSeSXrqnr0FBnlWR5YcWw0kjgMVXwDtGF7yeQ+r9I+oyAMGuVnpOUbWfG+6AtO09zt+MbrqW-y3Uer0Nqo9mdocnEFQJBmmcgqWpv1BDnrJnl7TFF7Belr8npe5k+AVr6aCgH6ygtgGeiBhi0wKc2+Eh1Ki2McxzMndwYAAKQgadcv1owFAQUBrWKrHSqpqI4lOKrCdMfn0yBuAIEnKBKndymunj5qogAKxDtAGcz7PoDznkLcqMa+sDTFdqG3ns26zaBc1pyT3eUW5r9CW-K58KgvW-mreM1vueGrdO6o46P2F-vz3c-WH0WlCYAAdQACUNN5B8jHuZZancp85meNzANW674JfHJXmbfndiWjdP02vXdy+pdL8uGY7kqFXHOtd7YP2NlrU4sQ+DCDci9U8vw6y+FiFHX2sAAC868yi7wPlYI+P9P6aRalFUK21rahlIY7UIzsEqpmSmxL2MMdDAEsIgRUsBgDYERoQZwrh0ZFSBkXG2icXKVTkhkdQAMv4tRABZPAR1f5c3lPIsYj8haMiiGItym8XwTEgb3LRMkj66MlqPQxAFjHvx5AYs+wVophQobbSKIVqGxixi7BhMggA)

([editable](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv3eQvqc7ta7dUzyJ9vlyrjz5zU6jAAGJWeIAWWNB7uR-dY-hUS3AalMDXB55XxXxlVVWdNTfbE3WeE82Q5C9SVNTNwz9SoRTFFCoJ1T9enYKIUN9SNl1UXCRwIi0iP9EcY38eMk1TdNCOzXM0HzbRdAMYwdBQO1Ky0fRmFrbxfEwOim16Vs+DPJI3jSdIuwkHs8mYv0o1HPCJyrQTRlU9Al09UjpRdIC1BQBAThQKjdMo7MtXfaEej1aSvlk405zDKj70fF8sMA+kyMMxFkVREiY3ImAkRRdTaMbBMYBTZNMDzAtOOLQYZArYYYAAcR5R5hPrMS4oZCLohyt4O3SLQeRU2y1Joj1xwRGBkAcPKygkGysz9AzmtXEycWOMBrL0tB7Og49XjPH5POzbznzDbCPya78YDA1DiMMgbD1M4aOskUYJpw2Dpq+WaACoFpfGqymWxzVvwmBbpQD5hj-FdAq6I4eTehwYtw8T4sS9MXr+sBWPYwsuKMbA9CgbALPgEDVAOjwRIbAJmFW1sEmSeSXrqnr0FBnlWR5YcWw0kjgMVXwDtGF7yeQ+r9I+oyAMGuVnpOUbWfG+6AtO09zt+MbrqW-y3Uer0Nqo9mdocnEFQJBmmcgqWpv1BDnrJnl7TFF7Belr8npe5k+AVr6aCgH6ygtgGeiBhi0wKc2+Eh1Ki2McxzMndwYAAKQgadcv1owFAQUBrWKrHSqpqI4lOKrCdMfn0yBuAIEnKBKndymunj5qogAKxDtAGcz7PoDznkLcqMa+sDTFdqG3ns26zaBc1pyT3eUW5r9CW-K58KgvW-mreM1vueGrdO6o46P2F-vz3c-WH0WlCYAAdQACUNN5B8jHuZZancp85meNzANW674JfHJXmbfndiWjdP02vXdy+pdL8uGY7kqFXHOtd7YP2NlrU4sQ+DCDci9U8vw6y+FiFHX2sAAC868yi7wPlYI+7sciQOtk9KKoVtrW1DGQx2oRnYJVTMlNiXsYY6GAJYRAipYDAGwIjQgzhXDoyKkDIuNtE4uUqnJDI6gAZfxaiACyeAjq-y5vKBRYxH5C0ZFEcRblN4vgmMQ4WOij56MlqPXu2iZImJ8rrO6n9NItWoWFShttIohRobGLGLtGEyCAA))
