/*
 ************************************************************************************
 * Copyright (C) 2001-2011 encuestame: system online surveys Copyright (C) 2011
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.business.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.encuestame.business.setup.install.InstallDatabaseOperations;
import org.encuestame.business.setup.install.TypeDatabase;
import org.encuestame.core.config.AdministratorProfile;
import org.encuestame.core.config.EnMePlaceHolderConfigurer;
import org.encuestame.core.filter.RequestSessionMap;
import org.encuestame.core.service.AbstractBaseService;
import org.encuestame.core.service.SetupOperations;
import org.encuestame.core.service.imp.SecurityOperations;
import org.encuestame.persistence.exception.EnmeFailOperation;
import org.encuestame.utils.web.UserAccountBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description.
 *
 * @author Picado, Juan juanATencuestame.org
 * @since Sep 9, 2011
 */
@Service(value = "setupService")
public class SetupService extends AbstractBaseService implements
        SetupOperations {

    /** Log. **/
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Install database operations.
     */
    @Autowired
    private InstallDatabaseOperations install;

    @Autowired
    private SecurityOperations securityOperations;

    /**
     * Set {@link InstallDatabaseOperations}.
     *
     * @param install
     *            the install to set
     */
    public void setInstall(final InstallDatabaseOperations install) {
        this.install = install;
    }

    private String getTypeDatabase() {
        final String typeDatabase = EnMePlaceHolderConfigurer
                .getConfigurationManager().getProperty("database.type");
        return typeDatabase;
    }

    /**
     * @throws EnmeFailOperation
     * @throws IOException
     *
     */
    @Override
    public String installDatabase() {
        log.debug("installDatabase.....");
        try {
            this.install.initializeDatabase(TypeDatabase
                    .getTypeDatabaseByString(this.getTypeDatabase()));
        } catch (Exception e) {
            log.fatal(e);
            RequestSessionMap.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return "fail";
        }
        return "ok";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.encuestame.core.service.SetupOperations#upgradeDatabase()
     */
    @Override
    public void upgradeDatabase() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.encuestame.core.service.SetupOperations#getSQLExecuted()
     */
    @Override
    public String getSQLExecuted() {
        log.debug("******************************************");
        log.debug("SQL " + this.install.getScriptLog());
        return this.install.getScriptLog();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.encuestame.core.service.SetupOperations#createAdmon(org.encuestame
     * .core.config.AdministratorProfile)
     */
    @Override
    public UserAccountBean createUserAdministration(
            AdministratorProfile administratorProfile) {
        log.debug("CREATE ADMON");
        return new UserAccountBean();
    }

    /**
     * @return the securityOperations
     */
    public SecurityOperations getSecurityOperations() {
        return securityOperations;
    }

    /**
     * @param securityOperations
     *            the securityOperations to set
     */
    public void setSecurityOperations(
            final SecurityOperations securityOperations) {
        this.securityOperations = securityOperations;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.encuestame.core.service.SetupOperations#removeTables()
     */
    @Override
    public Boolean removeTables() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.encuestame.core.service.SetupOperations#demoInstall()
     */
    @Override
    public void demoInstall() {
        try {
            this.install.installDemoData();
        } catch (Exception e) {
            log.fatal(e);
            RequestSessionMap.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }
}
