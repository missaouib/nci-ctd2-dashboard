package gov.nih.nci.ctd2.dashboard.impl;

import gov.nih.nci.ctd2.dashboard.model.Compound;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Proxy(proxyClass = Compound.class)
public class CompoundImpl extends SubjectImpl implements Compound {
    private String smilesNotation;

    @Column(length = 256, nullable = false)
    public String getSmilesNotation() {
        return smilesNotation;
    }

    public void setSmilesNotation(String smilesNotation) {
        this.smilesNotation = smilesNotation;
    }
}
