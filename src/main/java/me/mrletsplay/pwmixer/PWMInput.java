package me.mrletsplay.pwmixer;

import java.util.Objects;

public class PWMInput {

	protected long id;

	protected PWMInput(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PWMInput other = (PWMInput) obj;
		return id == other.id;
	}

}
