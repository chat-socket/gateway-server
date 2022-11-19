REPO=gateway-server
TIMESTAMP=tmp-$(shell date +%s )
DFILE=gateway-server/deployment.yml
SFILE=gateway-server/svc.yml
IFILE=gateway-server/ing.yml
VERSION=v1
TEST_PATH=Documents/projects/microservices-chat-app/gateway

.PHONY: update
update:
		@eval $$(minikube docker-env) ;\
		docker image build -t $(REPO):$(TIMESTAMP) -f Dockerfile .
		kubectl set image -n $(NSPACE) deployment/$(REPO) *=$(REPO):$(TIMESTAMP)

.PHONY: delete
delete:
		kubectl delete -n $(NSPACE) deployment,service $(REPO)

.PHONY: create
create:
		@eval $$(minikube docker-env) ;\
		docker image build -t $(REPO):$(VERSION) -f Dockerfile .
		kubectl create -f $(SFILE) -n $(NSPACE)
		kubectl create -f $(DFILE) -n $(NSPACE)

.PHONY: ingress
ingress:
		kubectl create -f $(IFILE)

.PHONY: push
push: build
		docker tag $(REPO):$(VERSION) $(REPO):latest
		docker push $(REPO):$(VERSION)
		docker push $(REPO):latest

build:
		@eval $$(minikube docker-env -u);\
		docker image build -t $(REPO):$(VERSION) -f Dockerfile .

.PHONY: remote
remote:
		rsync -Pva --exclude build --exclude=".*" . $(REMOTE_SERVER):$(TEST_PATH)
		ssh -t -l $(REMOTE_USER) $(REMOTE_SERVER) "cd ${TEST_PATH} ; bash --login" < /dev/tty
