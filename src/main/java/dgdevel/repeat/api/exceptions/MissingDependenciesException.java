package dgdevel.repeat.api.exceptions;

import java.util.Arrays;

import lombok.experimental.Accessors;

@Accessors(fluent=true)
public class MissingDependenciesException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String[] dependencies;

	public String[] dependencies() {
		String[] dependencies = new String[this.dependencies.length];
		System.arraycopy(this.dependencies, 0, dependencies, 0, dependencies.length);
		return dependencies;
	}

	public MissingDependenciesException(String[] dependencies) {
		super("missing dependencies: " + Arrays.toString(dependencies));
		this.dependencies = new String[dependencies.length];
		System.arraycopy(dependencies, 0, this.dependencies, 0, dependencies.length);
	}
}
