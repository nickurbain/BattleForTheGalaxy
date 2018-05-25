package data;

public class DoubloonsData extends JsonHeader{
	
	private int amount;
	
	public DoubloonsData(int amount) {
		super(JsonHeader.ORIGIN_CLIENT, JsonHeader.TYPE_DOUBLOONS);
		setAmount(amount);
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
