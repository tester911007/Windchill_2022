package ru.ruselprom.tasks.rules;

import org.apache.log4j.Logger;
import wt.enterprise.EnterpriseHelper;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.IBAHolder;
import wt.iba.value.IBAValueUtility;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.service.IBAValueHelper;
import wt.inf.container.WTContainerRef;
import wt.log4j.LogR;
import wt.rule.algorithm.InvalidAlgorithmArgumentException;
import wt.rule.algorithm.RuleAlgorithm;
import wt.util.WTContext;
import wt.util.WTException;

import java.rmi.RemoteException;
import java.util.Locale;

public class CustomNumberRule implements RuleAlgorithm {
    private static final String RULE_RESOURCE = "wt.rule.ruleResource";
    private static final Logger logger = LogR.getLogger("wt.content");
    private static final String IBA_NAME = "ИНДЕКС_ТС";

    @Override
    public Object calculate(Object[] args, WTContainerRef wt_container_ref) throws WTException {
        for (int symbol = 0; symbol < args.length; ++symbol) {
            if (args[symbol] == null) {
                throw new InvalidAlgorithmArgumentException("wt.rule.ruleResource", "387", new Object[]{symbol + 1});
            }
        }
        String num = EnterpriseHelper.getNumber(args);

        String iba_value = "";
        try {
            String attribute_name;
            IBAHolder obj_iba_holder = (IBAHolder) wt_container_ref.getObject();
            IBAHolder iba_holder = IBAValueHelper.service.refreshAttributeContainer(obj_iba_holder, null, WTContext.getContext().getLocale(), null);
            DefaultAttributeContainer default_attribute_container = (DefaultAttributeContainer) iba_holder.getAttributeContainer();

            for (AbstractValueView abstract_value_view : default_attribute_container.getAttributeValues()) {
                attribute_name = abstract_value_view.getDefinition().getName();
                if (IBA_NAME.equalsIgnoreCase(attribute_name)) {
                    iba_value = IBAValueUtility.getLocalizedIBAValueDisplayString(abstract_value_view, Locale.getDefault());
                    break;
                }
            }
        } catch (RemoteException | WTException e) {
            logger.error("CustomNumberRule Error: " + e);
        }

        if (iba_value.length() > 0) {
            num = iba_value + "." + num;
        }

        return num;
    }

    public void validateArgs(String[] args) throws InvalidAlgorithmArgumentException {
    }
}
