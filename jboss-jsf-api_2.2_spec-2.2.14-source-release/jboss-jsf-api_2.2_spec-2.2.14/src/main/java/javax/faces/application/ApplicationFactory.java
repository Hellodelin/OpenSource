/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.application;

import javax.faces.FacesWrapper;


/**
 * <p><strong class="changed_modified_2_0">ApplicationFactory</strong> is a 
 * factory object that creates
 * (if needed) and returns {@link Application} instances.  Implementations of
 * JavaServer Faces must provide at least a default implementation of
 * {@link Application}.</p>
 *
 * <p>There must be one {@link ApplicationFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   ApplicationFactory factory = (ApplicationFactory)
 *    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
 * </pre>
 */

public abstract class ApplicationFactory implements FacesWrapper<ApplicationFactory> {
    
    public ApplicationFactory() {
        
    }


    /**
     * <p class="changed_added_2_0">If this factory has been decorated, the 
     * implementation doing the decorating may override this method to provide
     * access to the implementation being wrapped.  A default implementation
     * is provided that returns <code>null</code>.</p>
     * 
     * @since 2.0
     */
    public ApplicationFactory getWrapped() {
        return null;
    }
    
    /**
     * <p>Create (if needed) and return an {@link Application} instance
     * for this web application.</p>
     */
    public abstract Application getApplication();


    /**
     * <p>Replace the {@link Application} instance that will be
     * returned for this web application.</p>
     *
     * @throws NullPointerException if <code>application</code>
     *  is <code>null</code>.

     * @param application The replacement {@link Application} instance
     */
    public abstract void setApplication(Application application);


}
