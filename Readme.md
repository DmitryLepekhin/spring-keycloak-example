### Spring + Keycloak example

The readme describes initial setup of the Keycloak server for the project.

#### Database setup

   * https://www.keycloak.org/docs/latest/server_installation/index.html#_database
   * https://medium.com/@pratik.dandavate/setting-up-keycloak-standalone-with-mysql-database-7ebb614cc229

1. *(optional)* Setup MySql
```sql
CREATE USER 'keycloak'@'localhost' IDENTIFIED BY 'keycloak';
CREATE DATABASE keycloak CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'localhost';
```

2. Package MySql driver JAR into a module
Download and copy mysql driver into keycloak-11.0.2\modules\system\layers\keycloak\com\mysql\main.
In the same directory create module.xml with the mysql module definition.
See example of the module.xml in the ./libs folder.

3. Point Keycloak to the Mysql
 Replace H2 settings to Mysql into *standalone/configuration/standalone.xml*:
 ```
  <drivers>
    <driver name="mysql" module="com.mysql">
        <xa-datasource-class>com.mysql.jdbc.Driver</xa-datasource-class>
    </driver>
  ...
```
and
```
  <datasources>
    <datasource jndi-name="java:/jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true">
        <connection-url>jdbc:mysql://localhost:3306/keycloak?useSSL=false&amp;characterEncoding=UTF-8</connection-url>
        <driver>mysql</driver>
        <pool>
            <min-pool-size>5</min-pool-size>
            <max-pool-size>15</max-pool-size>
        </pool>
        <security>
            <user-name>keycloak</user-name>
            <password>keycloak</password>
        </security>
        <validation>
            <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
            <validate-on-match>true</validate-on-match>
            <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
        </validation>
    </datasource>
  ...
```
and check that the datasource is used here:
```
<spi name="connectionsJpa">
...
```

4. Load the module
Start jboss-cli.bat and execute:
```
 module add --name=com.mysql --resources=C:\projects\kapow\zebra\keycloak\keycloak-11.0.2\modules\system\layers\keycloak\com\mysql\main\mysql-connector-java-5.1.26-bin.jar --dependencies=javax.api,javax.transaction.
api
```

5. Port offset.
It is not a database configuration, but it is the right moment to change the server port
if you don't want keycloak to occupy 8080. Set port-offset in the standalone.xml
If port-offset = 1111, then keycloak will be running on 9191.

6. Start standalone.bat

#### Configure the Client

1. Create initial admin user and the client
Go to http://localhost:9191/auth, create admin user and go to "Administration Console"

2. Create *Kofax RPA* realm, client, ...
On the "Administration Console" create a new client manually. Later we will export the client
and will import during installation.

