package net.weweave.tubewarder.domain;

public interface ExposableId extends IdObject {
	void setExposableId(String id);
	String getExposableId();
}
