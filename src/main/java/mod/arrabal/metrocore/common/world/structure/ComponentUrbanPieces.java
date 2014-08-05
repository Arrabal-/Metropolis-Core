package mod.arrabal.metrocore.common.world.structure;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 3/5/14.
 */
public class ComponentUrbanPieces {

    private static final String __OBFID = "CL_A00000102";

    public static void registerUrbanPieces()
    {
        MapGenStructureIO.func_143031_a(ComponentUrbanPieces.Metropolis.class, "Metro");
        MapGenStructureIO.func_143031_a(ComponentUrbanPieces.CitySquare.class, "Square");
        MapGenStructureIO.func_143031_a(ComponentUrbanPieces.Start.class, "Start");
    }

    public static List getComponentUrbanPiecesWeight(Random random, boolean isRuins) {

        ArrayList arraylist = new ArrayList();
        return null;
    }

    public static class PieceWeight {

        public Class urbanPieceClass;
        public final int urbanPieceWeight;
        public int urbanPiecesSpawned;
        public int urbanPiecesLimit;
        private static final String __OBFID = "CL_A0000103";

        public PieceWeight(Class urbanClass, int weight, int limit){
            this.urbanPieceClass = urbanClass;
            this.urbanPieceWeight = weight;
            this.urbanPiecesLimit = limit;
        }

        public boolean canSpawnMoreUrbanPieces(){
            return this.urbanPiecesLimit == 0 || this.urbanPiecesSpawned < this.urbanPiecesLimit;
        }
    }

    abstract static class Metropolis extends StructureComponent {

        protected enum RoadLayout {GRID, WEB, SPRAWL, MIXED}
        protected enum CentralSquare {MONUMENT, FOUNTAIN, PARK, PLAZA, GROVE}
        protected int baseHeight = -1;

        private int genRadiusX;
        private int genRadiusZ;
        private boolean isRuins;
        private RoadLayout gridType;
        private CentralSquare squareType;
        private int peopleSpawned;
        private ComponentUrbanPieces.Start startPiece;

        private static final String __OBFID = "CL_A0000201";

        public Metropolis() {}

        public Metropolis(ComponentUrbanPieces.Start startingPiece, int avgHeight,  int componentType){

            super(componentType);
            if (startPiece != null){
                this.isRuins = startingPiece.isRuins;
                this.genRadiusX = startingPiece.genX;
                this.genRadiusZ = startingPiece.genZ;
                this.gridType = startingPiece.gridType;
                this.squareType = startingPiece.centralSquare;
                this.baseHeight = avgHeight;
                startPiece = startingPiece;
            }
        }

        @Override
        //TODO:  setNBTData()
        protected void func_143012_a(NBTTagCompound NBTTag){
            NBTTag.setInteger("HPos", this.baseHeight);
            NBTTag.setInteger("PCount", this.peopleSpawned);
            NBTTag.setBoolean("Ruins", this.isRuins);
            NBTTag.setInteger("Layout", this.gridType == RoadLayout.GRID ? 0 : this.gridType == RoadLayout.WEB ? 1: this.gridType == RoadLayout.SPRAWL ?
                2 : 3);
            NBTTag.setInteger("Square", this.squareType == CentralSquare.MONUMENT ? 0 : this.squareType == CentralSquare.FOUNTAIN ? 1 :
                this.squareType == CentralSquare.PARK ? 2 : this.squareType == CentralSquare.PLAZA ? 3 : 4);
        }

        @Override
        //TODO:  getNBTData()
        protected void func_143011_b(NBTTagCompound NBTTag){
            this.baseHeight = NBTTag.getInteger("HPos");
            this.peopleSpawned = NBTTag.getInteger("PCount");
            this.isRuins = NBTTag.getBoolean("Ruins");
            int i = NBTTag.getInteger("Layout");
            this.gridType = (i == 0 ? RoadLayout.GRID : i == 1 ? RoadLayout.WEB : i == 2 ? RoadLayout.SPRAWL : RoadLayout.MIXED);
            int j = NBTTag.getInteger("Square");
            this.squareType = (j == 0 ? CentralSquare.MONUMENT : j == 1 ? CentralSquare.FOUNTAIN : j == 2 ? CentralSquare.PARK :
                j == 3 ? CentralSquare.PLAZA : CentralSquare.GROVE);

        }

        @Override
        protected void placeBlockAtCurrentPosition(World worldObj, Block block, int blockMeta, int horizOffset1, int vertOffset,
                                                        int horizOffset2, StructureBoundingBox structBB){

        }

        @Override
        protected void fillWithBlocks(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
                                      Block placeBlock, Block replaceBlock, boolean alwaysReplace){

        }

        @Override
        protected void fillWithMetadataBlocks(World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
                                      Block placeBlock, int placeBlockMeta, Block replaceBlock, int replaceBlockMeta, boolean alwaysReplace){

        }

        @Override
        //TODO:  replaceAirAndWaterBlocksDown()
        protected void func_151554_b(World worldObj, Block replaceBlock, int blockMeta, int horizOffset1, int vertOffset,
                                     int horizOffset2, StructureBoundingBox structBB){

        }
    }

    public static class CitySquare extends Metropolis{

        private static final String __OBFID = "CL_A0000202";
        protected CentralSquare centralSquare;
        protected boolean smallSquare;

        public CitySquare() {}

        public CitySquare(ComponentUrbanPieces.Start startingPiece, int avgHeight, int componentType, Random random, int minXPos, int minZPos){
            super(startingPiece, avgHeight, componentType);
            this.coordBaseMode = random.nextInt(4);
            this.smallSquare = (random.nextInt(5) < 2) ? true : false;
            switch (random.nextInt(6)) {
                case 0:
                    this.centralSquare = CentralSquare.FOUNTAIN;
                    break;
                case 1:
                    this.centralSquare = CentralSquare.GROVE;
                    break;
                case 2:
                    this.centralSquare = CentralSquare.MONUMENT;
                    break;
                case 3:
                    this.centralSquare = CentralSquare.PARK;
                    break;
                default:
                    this.centralSquare = CentralSquare.PLAZA;
            }
            switch (this.coordBaseMode){
                case 0:
                case 2:
                    if (this.smallSquare && this.centralSquare != CentralSquare.MONUMENT){
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 12 - 1, avgHeight + 5, minZPos + 12 - 1);
                    }
                    else if (this.centralSquare != CentralSquare.MONUMENT) {
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 16 - 1, avgHeight + 10, minZPos + 16 - 1);
                    }
                    else {
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 16 - 1, avgHeight + 20, minZPos + 16 - 1);
                    }
                    break;
                default:
                    if (this.smallSquare && this.centralSquare != CentralSquare.MONUMENT){
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 12 - 1, avgHeight + 5, minZPos + 12 - 1);
                    }
                    else if (this.centralSquare != CentralSquare.MONUMENT) {
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 16 - 1, avgHeight + 10, minZPos + 16 - 1);
                    }
                    else {
                        this.boundingBox = new StructureBoundingBox(minXPos, avgHeight, minZPos, minXPos + 16 - 1, avgHeight + 20, minZPos + 16 - 1);
                    }
            }

        }

        @Override
        // Initial phase of building component.  Identifies appropriate bounding box area to construct the component
        public void buildComponent(StructureComponent structComponent, List componentList, Random random){
            //step 1 determine the orientation for the square and adjust bounding box as appropriate
            switch (random.nextInt(3)){
                case 0:
                    // symmetrical city square
                    break;
                case 1:
                    // north/south orientation
                    if (this.centralSquare != CentralSquare.FOUNTAIN && this.centralSquare != CentralSquare.MONUMENT){
                       //"North/south should have the long side"
                        int offset = (this.smallSquare ? random.nextInt(2) + 1 : random.nextInt(4) + 1);
                        structComponent.getBoundingBox().minX += offset;
                        structComponent.getBoundingBox().maxX -= offset;
                    }
                    break;
                case 2:
                    // east/west orientation
                    if (this.centralSquare != CentralSquare.FOUNTAIN && this.centralSquare != CentralSquare.MONUMENT){
                        //"East/west should have the long side"
                        int offset = (this.smallSquare ? random.nextInt(2) + 1 : random.nextInt(4) + 1);
                        structComponent.getBoundingBox().minZ += offset;
                        structComponent.getBoundingBox().maxZ -= offset;
                    }
                    break;
                default:
                    //CitySquare.buildComponent random.nextInt(3) invalid int value returned
            }

            //step 2 Finalize bounding box area for roads leading into the square, based on the grid type for the urban area


        }

        @Override
        // Second phase of component generation.  Places component blocks in the world
        public boolean addComponentParts(World worldObj, Random random, StructureBoundingBox structBB) {
            this.fillWithBlocks(worldObj, structBB, structBB.minX, structBB.minY, structBB.minZ, structBB.maxX, structBB.minY, structBB.maxZ,
                    net.minecraft.init.Blocks.cobblestone, net.minecraft.init.Blocks.dirt, true);
            return false;
        }
    }

    public static class Start extends CitySquare{

        private WorldChunkManager worldChunkMngr;

        public boolean isRuins;
        public int terrainType;
        public PieceWeight structUrbanPieceWeight;
        public List structureUrbanWeightedPieceList;
        public List buildingList = new ArrayList();
        public List roadList = new ArrayList();
        public RoadLayout gridType;
        public int genX;
        public int genZ;

        private static final String __OBFID = "CL_A0000203";

        public BiomeGenBase biome;

        public Start() {}

        public Start(WorldChunkManager wChunkManager, int avgHeight, int componentType, Random random, int minX, int minZ,
                     int radiusX, int radiusZ, List weightedComponentList, int terrainType){
            super((ComponentUrbanPieces.Start)null, avgHeight, 0, random, minX, minZ);
            this.worldChunkMngr = wChunkManager;
            this.structureUrbanWeightedPieceList = weightedComponentList;
            this.terrainType = terrainType;
            this.biome = wChunkManager.getBiomeGenAt(minX, minZ);
            this.genX = radiusX;
            this.genZ = radiusZ;
            switch (random.nextInt(10)) {
                case 0:
                    this.gridType = RoadLayout.WEB;
                    break;
                case 1:
                case 2:
                case 4:
                    this.gridType = RoadLayout.SPRAWL;
                    break;
                case 5:
                    this.gridType = RoadLayout.WEB;
                    break;
                case 9:
                    this.gridType = RoadLayout.MIXED;
                    break;
                default:
                    this.gridType = RoadLayout.GRID;
            }
            this.isRuins = (random.nextInt(20) < 2);
        }

        public WorldChunkManager getWorldChunkManager() { return this.worldChunkMngr;}

    }
}
