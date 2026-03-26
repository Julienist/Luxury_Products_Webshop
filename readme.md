# Over deze repository
Dit is de repository van Julien Mijnders (s1142531) - dit was een opdracht aan de hogeschool leiden
Ik bouw in deze repo aan een webshop en verwerk daarin de feature "Promocodes".

# Installeren
1. `git clone git@gitlab.inf-hsleiden.nl:s1142531/INSOFAD_s1142531.git`
1. Ga in de frontend directory.
2. `npm install`
3. Ga in de backend directory.
4. Zorg ervoor dat alle dependencies in de pom.xml zijn geinstalleerd.

# Met docker starten
1. Je zult een .env file nodig hebben volg hiervoor het gegeven `.env.template`.
GEBRUIK DE ENV. VAR'S NIET IN PRODUCTIE.
Voor in de backend is er een application.properties file nodig.
2. `docker-compose up --build --no-cache -d` zou als goed is de applicatie moeten laten draaien.

# Starten met IDE
1. Voor in de backend is er een application.properties file nodig.
2. Start met `ng serve` voor in webstorm of welke IDE dan ook.
3. intellij heeft voor de backend in feite een mooie groene start knop.
Er is hier vast ook een command voor.
