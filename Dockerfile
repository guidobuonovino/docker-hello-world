# Usa un'immagine di base di Java
FROM openjdk:11-jre-slim

# Imposta la directory di lavoro all'interno del contenitore
WORKDIR /app

# Copia il tuo JAR Spring Boot nell'immagine Docker
COPY target/docker-hello-world.jar docker-hello-world.jar

# Esposizione della porta su cui l'applicazione Spring Boot ascolterà le richieste
EXPOSE 8080

# Comando per eseguire l'applicazione quando il contenitore viene avviato
CMD ["java", "-jar", "docker-hello-world.jar"]
