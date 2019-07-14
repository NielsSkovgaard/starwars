package com.b2w.starwars.infrastructure.mappers;

import com.b2w.starwars.core.DomainObject;
import com.b2w.starwars.core.InfrastructureObject;

public interface DomainInfrastructureMapper<TDomain extends DomainObject, TInfrastructure extends InfrastructureObject> {
    TInfrastructure map(TDomain domainObject);

    TDomain map(TInfrastructure infrastructureObject);
}
