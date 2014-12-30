package mod.arrabal.metrocore.common.library;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Arrabal on 12/29/2014.
 *
 * This code borrows heavily from biomesoplenty.common.utils.block.BlockStateUtils and the Biomes-o-Plenty team
 * are the core authors of this code.  Other than name changes, alterations from the BOP team code is indicated
 * below within comments
 */
public class BlockStateHelper {

    public static IProperty getPropertyByName(IBlockState blockState, String propertyName) {

        for (IProperty property : (ImmutableSet<IProperty>) blockState.getProperties().keySet()) {
            if (property.getName().equals(propertyName)) return property;
        }

        return null;
    }

    public static boolean isPropertyNameValid(IBlockState blockState, String propertyName) {
        return getPropertyByName(blockState, propertyName) != null;
    }

    public static Comparable getPropertyValueByName(IBlockState blockState, IProperty property, String valueName){

        for (Comparable value : (ImmutableSet<Comparable>)property.getAllowedValues()){
            if (value.toString().equals(valueName)) return value;
        }
        return null;
    }

    public static ImmutableSet<IBlockState> getValidStatesForProperties(IBlockState baseState, IProperty... properties){

        if (properties == null) return null;

        Set<IBlockState> validStates = Sets.newHashSet();
        PropertyIndexer indexer = new PropertyIndexer(properties);

        do{
            IBlockState currentState = baseState;
            for (IProperty property : properties){
                IndexedProperty indexedProperty = indexer.getIndexedProperty(property);
                currentState = currentState.withProperty(property, indexedProperty.getCurrentValue());
            }

            validStates.add(currentState);
        } while (indexer.increment());

        return ImmutableSet.copyOf(validStates);
    }

    private static class PropertyIndexer {

        private HashMap<IProperty, IndexedProperty> indexedProperties = new HashMap();

        private IProperty finalProperty;

        private PropertyIndexer(IProperty... properties){

            finalProperty = properties[properties.length - 1];

            IndexedProperty prevIndexedProperty = null;

            for (IProperty property : properties){
                IndexedProperty indexedProperty = new IndexedProperty(property);
                if (prevIndexedProperty != null){
                    indexedProperty.parent = prevIndexedProperty;
                    prevIndexedProperty.child = indexedProperty;
                }
                indexedProperties.put(property, indexedProperty);
                prevIndexedProperty = indexedProperty;
            }
        }

        public boolean increment(){
            return indexedProperties.get(finalProperty).increment();
        }

        public IndexedProperty getIndexedProperty(IProperty property){
            return indexedProperties.get(property);
        }
    }

    private static class IndexedProperty{

        private ArrayList<Comparable> validValues = new ArrayList();

        private int maxCount;
        private int counter;

        private IndexedProperty parent;
        private IndexedProperty child;

        private IndexedProperty(IProperty property){
            this.validValues.addAll(property.getAllowedValues());
            this.maxCount = this.validValues.size() - 1;
        }

        public boolean increment(){

            if (counter < maxCount) counter++;
            else{
                if (hasParent()){
                    resetSelfAndChildren();
                    return this.parent.increment();
                }
                else return false;
            }
            return true;
        }

        public void resetSelfAndChildren(){

            counter = 0;
            if (this.hasChild()) this.child.resetSelfAndChildren();
        }

        public boolean hasParent(){
            return parent != null;
        }

        public boolean hasChild(){
            return child != null;
        }

        public int getCounter(){
            return counter;
        }

        public int getMaxCount(){
            return maxCount;
        }

        public Comparable getCurrentValue(){
            return validValues.get(counter);
        }
    }
}
