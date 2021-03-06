/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.models.form.impl.v1;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class FormsHelperStubber {

    //the class to stub
    private static String CLASS_NAME = "com.day.cq.wcm.foundation.forms.FormsHelper";

    //the static field which is causing problems with class loadign due to missing impl class
    private static String ERROR_FIELD = "defaultFormStructureHelper";

    private FormsHelperStubber() {
    }

    protected static void createStub() {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass;
        try {
            ctClass = classPool.get(CLASS_NAME);
            //indicates the class has already been stubbed and loaded
            if (ctClass.isFrozen()) {
                return;
            }
            //set the body of all methods in the class to empty,
            //to remove any dependencies on impl classes.
            for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                ctMethod.setBody(null);
            }
            //remove the error causing static field declaration
            ctClass.removeField(ctClass.getDeclaredField(ERROR_FIELD));
            //remove the static initilizer block calling new on impl class.
            ctClass.removeConstructor(ctClass.getClassInitializer());
            //load the stubbed class
            ctClass.toClass();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}