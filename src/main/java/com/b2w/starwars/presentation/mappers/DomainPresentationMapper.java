package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.core.DomainObject;
import com.b2w.starwars.core.PresentationCreateObject;
import com.b2w.starwars.core.PresentationObject;

public interface DomainPresentationMapper<TDomain extends DomainObject, TPresentation extends PresentationObject, TPresentationCreate extends PresentationCreateObject> {
    TPresentation map(TDomain domainObject);

    TDomain map(TPresentationCreate presentationCreateObject);
}
