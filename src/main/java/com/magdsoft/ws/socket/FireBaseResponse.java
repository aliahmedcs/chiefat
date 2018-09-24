package com.magdsoft.ws.socket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FireBaseResponse {

    public Integer multicastId;
    public Integer success;
    public Integer failure;
    public Integer canonicalIds;

    public Integer getMulticast_id() {
		return multicastId;
	}

	public void setMulticast_id(Integer multicast_id) {
		this.multicastId = multicast_id;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailure() {
		return failure;
	}

	public void setFailure(Integer failure) {
		this.failure = failure;
	}

	public Integer getCanonical_ids() {
		return canonicalIds;
	}

	public void setCanonical_ids(Integer canonical_ids) {
		this.canonicalIds = canonical_ids;
	}

	public FireBaseResponse() {}
}