package component.bus;

/**
 * permet de gerer l'état du bus
 * @author le_kr_000
 *
 */
enum BusState {
	/**
	 * le bus est inoccupé
	 */
	FREE,
	/**
	 * le bus à été apelé par un composant
	 */
	CALLED,
	/**
	 * le bus est en train de transmettre des données
	 */
	TRANSMITING
}
