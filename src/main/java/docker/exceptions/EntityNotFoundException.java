package docker.exceptions;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -4329545024572346833L;

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
