/*
 * Copyright (c) 2010-2013 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.repo.sql.data.common;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.prism.path.ItemPath;
import com.evolveum.midpoint.repo.sql.data.common.embedded.RCredentials;
import com.evolveum.midpoint.repo.sql.util.DtoTranslationException;
import com.evolveum.midpoint.repo.sql.util.RUtil;
import com.evolveum.midpoint.schema.GetOperationOptions;
import com.evolveum.midpoint.schema.SelectorOptions;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.AccountShadowType;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.util.Collection;

/**
 * @author lazyman
 */
@Entity
@ForeignKey(name = "fk_account_shadow")
public class RAccountShadow extends RShadow<AccountShadowType> {

    private String accountType;
    private RCredentials credentials;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Embedded
    public RCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(RCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RAccountShadow that = (RAccountShadow) o;

        if (accountType != null ? !accountType.equals(that.accountType) : that.accountType != null) return false;
        if (credentials != null ? !credentials.equals(that.credentials) : that.credentials != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (accountType != null ? accountType.hashCode() : 0);
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        return result;
    }

    public static void copyToJAXB(RAccountShadow repo, AccountShadowType jaxb, PrismContext prismContext,
                                  Collection<SelectorOptions<GetOperationOptions>> options) throws
            DtoTranslationException {
        RShadow.copyToJAXB(repo, jaxb, prismContext, options);

        jaxb.setAccountType(repo.getAccountType());

        if (repo.getCredentials() != null) {
            ItemPath path = new ItemPath(AccountShadowType.F_CREDENTIALS);
            jaxb.setCredentials(repo.getCredentials().toJAXB(jaxb, path, prismContext));
        }
    }

    public static void copyFromJAXB(AccountShadowType jaxb, RAccountShadow repo,
                                    PrismContext prismContext) throws DtoTranslationException {
        RShadow.copyFromJAXB(jaxb, repo, prismContext);

        repo.setAccountType(jaxb.getAccountType());

        if (jaxb.getCredentials() != null) {
            RCredentials credentials = new RCredentials();
            RCredentials.copyFromJAXB(jaxb.getCredentials(), credentials, prismContext);
            repo.setCredentials(credentials);
        }
    }

    public AccountShadowType toJAXB(PrismContext prismContext, Collection<SelectorOptions<GetOperationOptions>> options)
            throws DtoTranslationException {
        AccountShadowType shadow = new AccountShadowType();
        RUtil.revive(shadow, prismContext);
        RAccountShadow.copyToJAXB(this, shadow, prismContext, options);

        return shadow;
    }
}