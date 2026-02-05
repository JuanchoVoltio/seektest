package com.seek.messaging.model;

public interface IProvider <Rq, P extends IPayload> {
    Rq getRequestParams(P payload);
}
