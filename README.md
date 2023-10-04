# Introduction 
...


# Build and Test
mvn clean package -P<profilo> per ottenere una fresh build 

# Installazione come pod openshift
riferire alle linee guida sui microservizi per dettagli.

I deployments files per ambiente openshift (facilmente modificabili per una qualsiasi installazione Kubernetes) sono nella directory 'k8s'. i deployments files sono dei fragments utilizzati dal plug-in maven [JKUBE](https://www.eclipse.org/jkube/docs/openshift-maven-plugin) (os mode) per generare dinamicamente i deployments files completi, durante la maven phase resources (oc-resources).
``` sh
mvn oc:resources -Pcoll
```
è importante specificare il profilo perchè il plugin non è caricato negli altri profili e si avrebbe un errore di plugin non definito.

Le proprietà dell'applicazione sono caricate tramite una config-map di kubernetes per separare il deployment dalla configurazione. La config map è generata dal plugin analizzando il fragment 'config-map.yaml' che dichiara il file di properties nei metadati come kubernetes annotation:
``` yaml
metadata:
  name: ${project.artifactId}
  annotations:
    maven.jkube.io/cm/application.properties: src/test/resources/test-application.properties
```

il plugin è configurato per inserire il contenuto del file di properties come valore di una key all'interno del config-map, di nome o application.properties o application.yml in base al valore della key dell'annotation (vedi nell'esempio).
L'applicazione spring-boot può caricare le properties dalla config-map in fase di avvio solamente se utilizza le librerie spring-cloud-kubernetes.

Le username, le password, etc devono essere generate in precedenza tramite il comando oc da un utenza con privilegi sul cluster kubernetes (od openshift) di installazione.
``` sh
 oc create secret generic docker-hello-world-secrets --from-literal=db_user=APP_AIFA --from-literal=db_password=app_aifa --from-literal=auth_username=app_aifa --from-literal=auth_password=app_aifa
```
(le password nella riga di comando sono in chiaro, il secret file creato sul cluster di destinazione sarà di tipo opaque, quindi criptate).

Il comando deve essere eseguito solamente la prima volta sull'ambiente target, da quel momento può essere referenziato da ogni pod e deployment che ne abbia bisogno.

Il fragment 'deployment.yaml' riferisce a tutte le altre risorse come i secret, il serviceaccount.yaml, etc per creare un oggetto di tipo deployment che istruisce openshift sul deploy del micro-servizio e sulla sua configurazione e aggiornamento (image-stream triggers, etc).

IMPORTANTE: in tutti i file è presente il namespace kubernetes del deployment (project nella  terminologia openshift). in ambiente collaudo AIFA è 'aifa-coll', potrebbe essere necessario modificarlo in base al namespace di destinazione che si ha a disposizione nell'ambiente target:
``` yml
metadata:
  namespace: aifa-coll
```
Dopo la fase oc:resources è possibile usare vari comandi di deployments per eseguire l'installazione:

``` sh
mvn oc:apply -Pcoll  # se si desidera solamente eseguire la creazione/update degli oggetti di deployments sul cluster di destinazione (ma non la pubblicazione della docker image e quindi la creazione del pod)

mvn oc:build -Pcoll  # se si desidera solamente eseguire il build dell'immagine docker

mvn oc:push -Pcoll  # se si desidera solamente eseguire la pubblicazione dell'immagine docker con una nuova tag version sul registro docker

mvn oc:deploy -Pcoll # se si desidera eseguire il deployment completo

mvn clean package oc:resource oc:deploy -DskipTests=true -Pcoll  # i goals del plugin jkube possono essere eseguiti in pipes
```

### details
If you want to learn more about creating good readme files then refer the following [guidelines](https://docs.microsoft.com/en-us/azure/devops/repos/git/create-a-readme?view=azure-devops). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)