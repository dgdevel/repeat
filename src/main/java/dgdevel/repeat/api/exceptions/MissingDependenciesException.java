package dgdevel.repeat.api.exceptions;

import java.util.Arrays;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
public class MissingDependenciesException extends Exception {

	private static final long serialVersionUID = 1L;

	@Getter
	private String[] dependencies;

	public MissingDependenciesException(String[] dependencies) {
		super("missing dependencies: " + Arrays.toString(dependencies));
		this.dependencies = dependencies;
	}
}
