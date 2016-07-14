package component.memory;

/**
 * cette classe g�re les m�moire � lecture seule
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.1.0
 *
 */
public abstract class AbsReadOnlyMemory extends AbsMemory {


	public AbsReadOnlyMemory()
	{
		super();
	}
	@Override
	public void write(int val, int adr) throws WRException {
		throw new WRException(null);

	}


}
