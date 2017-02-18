.SILENT:
EXCPT = ./exceptions
OBJ = ErrnoException.o DMMP.o Socket.o Convert.o Config.o AccessSerAp.o
GPP = g++ -m64 -Wall -pthread 

all: ServeurMouvements ApplicationMouvements 

ServeurMouvements: $(OBJ) SocketServeur.o SocketClient.o ServeurMouvements.cpp
	echo "Compilation ServeurMouvements"
	$(GPP) $(OBJ) SocketServeur.o SocketClient.o ServeurMouvements.cpp -o ServeurMouvements

ApplicationMouvements: $(OBJ) SocketServeur.o SocketClient.o ApplicationMouvements.cpp
	echo "Compilation ApplicationMouvements"
	$(GPP) $(OBJ) SocketServeur.o SocketClient.o ApplicationMouvements.cpp -o ApplicationMouvements

DMMP.o:	DMMP.h DMMP.cpp
	echo "Compilation DMMP"
	$(GPP) DMMP.cpp -c

AccessSerAp.o: AccessSerAp.h AccessSerAp.cpp
	echo "Compilation AccesSerAp"
	$(GPP) AccessSerAp.cpp -c

Convert.o: Convert.h Convert.cpp
	echo "Compilation Convert"
	$(GPP) Convert.cpp -c

Socket.o: Socket.h Socket.cpp
	echo "Compilation Socket"
	$(GPP) Socket.cpp -c

SocketClient.o: SocketClient.h SocketClient.cpp
	echo "Compilation SocketClient"
	$(GPP) SocketClient.cpp -c

SocketServeur.o: SocketServeur.h SocketServeur.cpp
	echo "Compilation SocketServeur"
	$(GPP) SocketServeur.cpp -c

ErrnoException.o:	$(EXCPT)/ErrnoException.h $(EXCPT)/ErrnoException.cpp
	echo "Compilation ErrnoException"
	$(GPP) $(EXCPT)/ErrnoException.cpp -c 

Config.o:	Config.h Config.cpp
	echo "Compilation Config"
	$(GPP) Config.cpp -c 

clean:
	echo "Netoyaaaaaaage !"
	@rm -f *.o *.gch $(EXCPT)/*.o ServeurMouvements ApplicationMouvements

