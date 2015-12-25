package tubewarder.domain;

import java.io.Serializable;

public interface IdObject extends Serializable {
    Long getId();
    void setId(Long id);
}
