import wt.enterprise.EnterpriseHelper;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.IBAHolder;
import wt.iba.value.IBAValueUtility;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.service.IBAValueHelper;
import wt.inf.container.WTContainerRef;
import wt.rule.algorithm.RuleAlgorithm;
import wt.util.WTContext;
import wt.util.WTException;

import java.rmi.RemoteException;
import java.util.Locale;

public class CustomNumberRule implements RuleAlgorithm {
    @Override
    public Object calculate(Object[] args, WTContainerRef wt_container_ref) {
        String iba_name = "ИНДЕКС_ТС";
        String iba_value = "";
        String num = "ОШИБКА ГЕНЕРАЦИИ";
        try {
            num = EnterpriseHelper.getNumber(args);
            String name;

            IBAHolder obj_iba_holder = (IBAHolder) wt_container_ref.getObject();
            IBAHolder iba_holder = IBAValueHelper.service.refreshAttributeContainer(obj_iba_holder, null, WTContext.getContext().getLocale(), null);
            DefaultAttributeContainer default_attribute_container = (DefaultAttributeContainer) iba_holder.getAttributeContainer();

            for (AbstractValueView abstract_value_view : default_attribute_container.getAttributeValues()) {
                name = abstract_value_view.getDefinition().getName();
                if (iba_name.equalsIgnoreCase(name)) {
                    iba_value = IBAValueUtility.getLocalizedIBAValueDisplayString(abstract_value_view, Locale.getDefault());
                    break;
                }
            }
        } catch (RemoteException | WTException e) {
            e.printStackTrace();
        }

        return iba_value + "." + num;
    }
}
