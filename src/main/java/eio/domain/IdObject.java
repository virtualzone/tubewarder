package eio.domain;

import java.io.Serializable;

/**
 * Created by heiner on 15/05/15.
 */
public interface IdObject extends Serializable {
    Long getId();
    void setId(Long id);
}
