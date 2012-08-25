package unit;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
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

import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.ui.Column;
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
		SniperSnapshot joining = SniperSnapshot.joining("item-id");
		SniperSnapshot bidding = joining.bidding(555, 666);
		context.checking(new Expectations(){{
			allowing(listener).tableChanged(with(anInsertionEvent()));
			one(listener).tableChanged(with(aChangeInRow(0)));
		}});

		model.addSniper(joining);
		model.sniperStateChanged(bidding);
		
		assertRowMatchesSnapshot(0, bidding);
	}
	
	@Test
	public void setsUpColumnHeadings(){
		for (Column column : Column.values()) {
			assertEquals(column.name, model.getColumnName(column.ordinal()));
		}
	}
	
	@Test public void notifiesListenersWhenAddingASniper(){
		SniperSnapshot joining = SniperSnapshot.joining("item-id");
		context.checking(new Expectations(){{
			one(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		assertEquals(0, model.getRowCount());
		
		model.addSniper(joining);
		
		assertEquals(1, model.getRowCount());
		assertRowMatchesSnapshot(0, joining);
	}
	
	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		for (Column column : Column.values()) {
			assertEquals(column.valueIn(snapshot), model.getValueAt(row, column.ordinal()));
		}
	}

	protected Matcher<TableModelEvent> anInsertionEvent() {
		return hasProperty("type", equalTo(TableModelEvent.INSERT));
	}

	protected Matcher<TableModelEvent> aChangeInRow(int row) {
		return hasProperty("firstRow", equalTo(row));
	}

	protected Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return allOf(anInsertionEvent(), aChangeInRow(row));
	}

}
