package com.metis.base.widget.adapter.delegate;

import com.metis.base.module.Footer;

/**
 * Created by Beak on 2015/7/8.
 */
public class FooterDelegate extends BaseDelegate<Footer> {

    private boolean isInStaggeredGrid = false;

    public FooterDelegate(Footer footer) {
        super(footer);
    }

    @Override
    public int getDelegateType() {
        return DelegateType.TYPE_FOOTER.getType();
    }

    public boolean isInStaggeredGrid() {
        return isInStaggeredGrid;
    }

    public void setIsInStaggeredGrid(boolean isInStaggeredGrid) {
        this.isInStaggeredGrid = isInStaggeredGrid;
    }
}
