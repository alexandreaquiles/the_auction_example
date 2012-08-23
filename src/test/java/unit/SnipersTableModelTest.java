package unit;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alexandreaquiles.auctionsniper.SniperState;
import br.com.alexandreaquiles.auctionsniper.ui.Column;
import br.com.alexandreaquiles.auctionsniper.ui.MainWindow;
import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;

@RunWith(JMock.class)
public class SnipersTableModelTest {
	private final Mockery context = new Mockery();
	private TableModelListener listener = context.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();
	
	@Before
	public void attachModelListener(){
		model.addTableModelListener(listener);
	}
	
	@Test
	public void hasEnoughColumns(){
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test
	public void setsSniperValuesInColumns(){
		context.checking(new Expectations(){{
			one(listener).tableChanged(with(aRowChangedEvent()));
		}});
		
		model.sniperStatusChanged(new SniperState("item-id", 555, 666), MainWindow.STATUS_BIDDING);
		
		assertColumnEquals(Column.ITEM_IDENTIFIER, "item-id");
	}
	
	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0;
		final int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
		
	}

	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}

}
