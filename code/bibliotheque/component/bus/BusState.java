package component.bus;

/**
 * permet de gerer l'�tat du bus
 * @author le_kr_000
 *
 */
enum BusState {
	/**
	 * le bus est inoccup�
	 */
	FREE,
	/**
	 * le bus � �t� apel� par un composant
	 */
	CALLED,
	/**
	 * le bus est en train de transmettre des donn�es
	 */
	TRANSMITING
}
