/**
 * Copyright (c) 2015 Evolveum
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
package com.evolveum.midpoint.web.component;

import javax.xml.namespace.QName;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ByteArrayResource;

import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.web.component.prism.ObjectWrapper;
import com.evolveum.midpoint.web.component.util.PrismPropertyWrapperModel;
import com.evolveum.midpoint.web.component.util.VisibleEnableBehaviour;
import com.evolveum.midpoint.xml.ns._public.common.common_3.FocusType;

/**
 * @author semancik
 *
 */
public abstract class FocusSummaryPanel<F extends FocusType> extends Panel {	
	private static final long serialVersionUID = -3755521482914447912L;
	
	private static final String ID_BOX = "summaryBox";
	private static final String ID_ICON_BOX = "summaryIconBox";
	private static final String ID_PHOTO = "summaryPhoto";
	private static final String ID_ICON = "summaryIcon";
	private static final String ID_DISPLAY_NAME = "summaryDisplayName";
	private static final String ID_IDENTIFIER = "summaryIdentifier";
	private static final String ID_TITLE = "summaryTitle";
	
	private static final String BOX_CSS_CLASS = "info-box";
	private static final String ICON_BOX_CSS_CLASS = "info-box-icon";

	public FocusSummaryPanel(String id, final IModel<ObjectWrapper<F>> model) {
		super(id, model);
		
		WebMarkupContainer box = new WebMarkupContainer(ID_BOX);
		add(box);
		
		box.add(new AttributeModifier("class", BOX_CSS_CLASS + " " + getBoxAdditionalCssClass()));
		
		box.add(new Label(ID_DISPLAY_NAME, new PrismPropertyWrapperModel<>(model, getDisplayNamePropertyName())));
		box.add(new Label(ID_IDENTIFIER, new PrismPropertyWrapperModel<>(model, getIdentifierPropertyName())));
		if (getTitlePropertyName() == null) {
			box.add(new Label(ID_TITLE, ""));
		} else {
			box.add(new Label(ID_TITLE, new PrismPropertyWrapperModel<>(model, getTitlePropertyName())));
		}
		
		WebMarkupContainer iconBox = new WebMarkupContainer(ID_ICON_BOX);
		box.add(iconBox);
		
		if (getIconBoxAdditionalCssClass() != null) {
			iconBox.add(new AttributeModifier("class", ICON_BOX_CSS_CLASS + " " + getIconBoxAdditionalCssClass()));
		}
		
		Image img = new Image(ID_PHOTO, new AbstractReadOnlyModel<AbstractResource>() {

            @Override
            public AbstractResource getObject() {
            	byte[] jpegPhoto = model.getObject().getObject().asObjectable().getJpegPhoto();
                if(jpegPhoto == null) {
                	return null;
                } else {
                    return new ByteArrayResource("image/jpeg",jpegPhoto);
                }
            }
        });
		img.add(new VisibleEnableBehaviour(){    		
            @Override
            public boolean isVisible(){
            	return model.getObject().getObject().asObjectable().getJpegPhoto() != null;
            }
        });
		iconBox.add(img);
        
        Label icon = new Label(ID_ICON,"");
        icon.add(new AttributeModifier("class", getIconCssClass()));
        icon.add(new VisibleEnableBehaviour(){    		
            @Override
            public boolean isVisible(){
            	return model.getObject().getObject().asObjectable().getJpegPhoto() == null;
            }
        });
        iconBox.add(icon);
	}
	
	protected abstract String getIconCssClass();
	
	protected abstract String getIconBoxAdditionalCssClass();
	
	protected abstract String getBoxAdditionalCssClass();

	protected QName getIdentifierPropertyName() {
		return FocusType.F_NAME;
	}

	protected abstract QName getDisplayNamePropertyName();
	
	protected QName getTitlePropertyName() {
		return null;
	}
	
}