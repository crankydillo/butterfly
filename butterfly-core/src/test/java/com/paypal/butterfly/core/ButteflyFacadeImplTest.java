package com.paypal.butterfly.core;

import com.paypal.butterfly.core.exception.InternalException;
import com.paypal.butterfly.extensions.api.Extension;
import com.paypal.butterfly.extensions.api.TransformationTemplate;
import com.paypal.butterfly.extensions.api.exception.ButterflyException;
import com.paypal.butterfly.extensions.api.upgrade.UpgradePath;
import com.paypal.butterfly.extensions.springboot.ButterflySpringBootExtension;
import com.paypal.butterfly.extensions.springboot.JavaEEToSpringBoot;
import com.paypal.butterfly.extensions.springboot.SpringBootUpgrade_1_5_6_to_1_5_7;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * ButteflyFacadeImplTest
 * <p>
 * Created by vkuncham on 11/7/2016.
 */
public class ButteflyFacadeImplTest extends PowerMockTestCase {

    @InjectMocks
    private ButterflyFacadeImpl butterflyFacadeImpl;

    @Mock
    private ExtensionRegistry extensionRegistry;

    @Mock
    private TransformationEngine transformationEngine;

    private ExtensionRegistry extensionRegistry_test = new ExtensionRegistry();

    private File applicationFolder = new File(this.getClass().getClassLoader().getResource("testTransformation").getFile());

    @Test
    public void testGetRegisteredExtension() {
        when(extensionRegistry.getExtension()).thenReturn(extensionRegistry_test.getExtension());
        Extension extension = butterflyFacadeImpl.getRegisteredExtension();
        Assert.assertNotNull(extension);
        Assert.assertTrue(extension instanceof ButterflySpringBootExtension);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Template class name cannot be blank")
    public void testTransformWithTemplateClassAsEmptyString() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, "");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Template class name cannot be blank")
    public void testTransformWithTemplateAsNull() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, (String) null);
    }


    @Test(expectedExceptions = InternalException.class, expectedExceptionsMessageRegExp = "Template class TestTemplate not found.*")
    public void testTransformWithInValidTemplate() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, "TestTemplate");
    }

    @Test
    public void testTransformWithValidTemplate() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, JavaEEToSpringBoot.class.getName());
        verify(transformationEngine, times(1)).perform((TemplateTransformation) anyObject());
    }

    @Test(expectedExceptions = InternalException.class, expectedExceptionsMessageRegExp = "Template class class com.paypal.butterfly.extensions.api.TransformationTemplate could not be instantiated.*")
    public void testTransformWithAbstractTemplate() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, TransformationTemplate.class.getName());
    }

    @Test
    public void testTransformWithValidTemplateAsClass() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, JavaEEToSpringBoot.class);
        verify(transformationEngine, times(1)).perform((TemplateTransformation) anyObject());
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Invalid application folder testTransformation1")
    public void testTransformWithValidUpgradePathInvalidAppFolder() throws ButterflyException {
        UpgradePath upgradePath = new UpgradePath(SpringBootUpgrade_1_5_6_to_1_5_7.class);
        butterflyFacadeImpl.transform(new File("testTransformation1"), upgradePath);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Upgrade path cannot be null")
    public void testTransformWithInValidUpgradePath() throws ButterflyException {
        butterflyFacadeImpl.transform(applicationFolder, (UpgradePath) null);
    }

    @Test
    public void testTransformWithValidUpgradePath() throws ButterflyException {
        UpgradePath upgradePath = new UpgradePath(SpringBootUpgrade_1_5_6_to_1_5_7.class);
        butterflyFacadeImpl.transform(applicationFolder, upgradePath);
        verify(transformationEngine, times(1)).perform((UpgradePathTransformation) anyObject());
    }
}
