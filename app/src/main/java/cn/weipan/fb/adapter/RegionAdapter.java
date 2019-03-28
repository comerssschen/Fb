package cn.weipan.fb.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.bean.RegionModel;

/**
 * 地区选择器
 * Created by cc on 2016/9/2.
 * 邮箱：904359289@QQ.com.
 */
public class RegionAdapter extends BaseQuickAdapter<RegionModel> {

    public RegionAdapter(List<RegionModel> data) {
        super(R.layout.item_list_region, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, RegionModel regionModel) {
        holder.setText(R.id.name, regionModel.getName());
    }
}
