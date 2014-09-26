package networking;

import gameLogic.gameState.Game;

public class GameServer extends Server {

	// Server-side version of the game
	Game serverSideGame = new Game(); // Ryan

	@Override
	public void retrieveObject(NetworkObject data) {

		if( data.getData() instanceof ChatMessage ){


		}
		else if( data.getData() instanceof ChatMessage ){


		}
	}

	@Override
	public void newClientConnection(ClientThread cl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientRejoins(ClientThread cl) {
		// TODO Auto-generated method stub

	}

}
